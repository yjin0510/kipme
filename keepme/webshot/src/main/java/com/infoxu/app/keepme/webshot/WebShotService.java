/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.keepme.webshot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.routines.UrlValidator;
import org.openqa.selenium.WebDriverException;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.collect.Lists;
import com.infoxu.app.keepme.data.Message;
import com.infoxu.app.keepme.data.RequestMessage;
import com.infoxu.app.keepme.data.Snapshot;
import com.infoxu.app.keepme.data.SnapshotMetaData;
import com.infoxu.app.keepme.monitor.Auditable;
import com.infoxu.app.keepme.monitor.Auditor;
import com.infoxu.app.keepme.queue.MessageQueueFactory;
import com.infoxu.app.keepme.queue.MessageQueueReceiver;
import com.infoxu.app.keepme.queue.MessageQueueSender;
import com.infoxu.app.keepme.queue.MessageQueueType;
import com.infoxu.app.keepme.webshot.MetaDataRetrieverFactory.MDRType;
import com.infoxu.app.keepme.webshot.SeleniumSnapshotRetriever.SSRType;
import com.infoxu.app.keepme.util.ServiceProperty;
import com.infoxu.app.keepme.util.Util;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * WebShot service: create service threads
 * Each thread: 
 * 1) Retrieve a message from request_queue
 * 2) Take snapshot and get meta data
 * 3) Construct a new message with all the obtained information
 * 4) Send the new message to both reply_queue and index_queue
 * 
 * This class should be implemented as a Daemon
 * @author yujin
 *
 */
public class WebShotService implements Daemon {
	private static Log logger = LogFactory.getLog(WebShotService.class);
	private static final int NUM_WORKERS = Integer.parseInt(ServiceProperty.getInstance()
			.getProperty("webshot.number.worker", "1"));
	
	// With an additional monitoring thread
	private ExecutorService executor = Executors.newFixedThreadPool(NUM_WORKERS + 1);
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		boolean stop = false;
		// For stopping the service
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		WebShotService wss = new WebShotService();
		wss.init(null);
		wss.start();
		logger.info("WebShotService test-run will only last for 1 hour.");
		Thread.sleep(3600000);
//		try {	
//			while (!stop) {
//				String line = br.readLine();
//				if (line.equalsIgnoreCase("stop")) {
//					break;
//				}
//			}
//		} catch (Exception e) {
//			logger.error("Exist with error: " + e.getMessage());
//		} finally {
//			
//		}
		wss.stop();
		wss.destroy();
		logger.info("WebShotService test-run finished.");
	}

	public void destroy() {
		executor.shutdown();
		logger.info("WebShotService cleared.");
	}

	public void init(DaemonContext arg0) throws DaemonInitException, Exception {
		Auditor auditor = new Auditor();
		executor.execute(auditor);
		
		// Create and register workers
		for (int i = 0; i < NUM_WORKERS; i++) {
			Worker worker = new Worker(i);
			executor.execute(worker);
			auditor.register(worker);
		}
		logger.info("WebShotService initialized.");
	}

	public void start() throws Exception {
		
	}
	
	public void stop() throws Exception {
		
	}

	// Worker class
	private class Worker extends Thread implements Auditable {
		private volatile boolean stop = false;
		private int tid;
		// Retrieve a message from the request queue
		private MessageQueueReceiver<RequestMessage> recvQ = MessageQueueFactory.getInstance()
				.getMessageQueueReceiver(MessageQueueType.REQUEST_QUEUE);
		// Reply to the reply_queue
		private MessageQueueSender<Message> sendQ = MessageQueueFactory.getInstance()
				.getMessageQueueSender(MessageQueueType.REPLY_QUEUE);
		private MessageQueueSender<Message> indexQ = MessageQueueFactory.getInstance()
				.getMessageQueueSender(MessageQueueType.INDEX_QUEUE);
		// One for each worker
		private SeleniumSnapshotRetriever ssr = 
				new SeleniumSnapshotRetriever(SSRType.SELENIUM_FIREFOX);
		// Counters
		private Map<Message.Status, Integer> counters = 
				new EnumMap<Message.Status, Integer>(Message.Status.class);
		
		public Worker(int tid) {
			this.tid = tid;
			stop = false;
		}
		
		public int getTid() {
			return this.tid;
		}

		@Override
		public void run() {
			while (!stop) {
				try {
					logger.info("Worker " + tid + " waiting for request");
					RequestMessage request = recvQ.getNext();
					logger.info("Worker " + tid + " received request: " 
							+ request.getReqId() + ", " + request.getUrl());
					Message reply = process(request);
					// Log the process results
					counters.put(reply.getStatus(), 
							counters.get(reply.getStatus()) == null ? 
									1 : counters.get(reply.getStatus()) + 1);
					
					logger.info("Worker " + tid + " get info " + reply.toString());
//					sendQ.send(reply);
					logger.debug("Worker " + tid + " send req " + request.getReqId() + " to reply_queue");
//					indexQ.send(reply);
					logger.debug("Worker " + tid + " send req " + request.getReqId() + " to index_queue");
				} catch (Exception e) {
					logger.error("Worker " + tid + " failed to complete the request " + e.getMessage());
					continue; // 
				}
			}
			
			// Clean up
			try {
				ssr.close();
				sendQ.close();
				recvQ.close();
				indexQ.close();
			} catch (Exception e) {
				logger.error("Worker " + tid + " failed to clean up after execution " + e.getMessage());
			}
		}
		
		private Message process(RequestMessage request) {
			// FAIL
			byte[] image = null;
			String reqUrl = Util.getValidURL(request.getUrl());
			// Validate URL
			if (reqUrl == null) {
				logger.error("Webshot Worker " + tid + " found an invalid URL " + reqUrl + " for reqId " 
						+ request.getReqId());
				Message reply = new Message(Message.Status.INVALID_URL, null, request);
				return reply;
			}
			
			URL url = null;
			try {
				url = new URL(request.getUrl());
				image = ssr.getSnapshot(url);
			} catch (MalformedURLException e) {
				logger.error("Webshot Worker " + tid + " failed to get snapshot for reqId " 
						+ request.getReqId() + " : "+ e.getMessage());				
				Message reply = new Message(Message.Status.FAIL_IMAGE, null, request);
				return reply;
			} catch (WebDriverException e) {
				logger.error("Thread " + this.tid + " receives exception from the driver: " 
						+ e.getCause().getMessage());
			}
			
			// INCOMPLETE
			String source = ""; // Page source code
			String urlStr = "";
			String ip = "";
			String domainDetails = ""; // Owner, host, etc., from whois lookup
			String digest = ""; // Image digest from SHA-1 256
			long timestamp = System.currentTimeMillis();
			String dns = url.getHost();
			
			Message reply = new Message();
			reply.setStatus(Message.Status.SUCCESS_IMAGE_METADATA);
			
			try {
				source = ssr.getSource();
				urlStr = ssr.getCurrentURL();
				digest = ssr.getDigest();
				
				ip = MetaDataRetrieverFactory
						.getMetaDataRetriever(MDRType.IP).getMetaData(url);
				domainDetails = MetaDataRetrieverFactory
						.getMetaDataRetriever(MDRType.DOMAIN_DETAILS).getMetaData(url);
			} catch (Exception e) {
				logger.error("Webshot Worker " + tid + " failed to get complete metadata for reqId " 
						+ request.getReqId() + " : "+ e.getMessage());
				reply.setStatus(Message.Status.INCOMPLETE_METADATA);
			}
			
			SnapshotMetaData metadata = new SnapshotMetaData(
					source, urlStr, ip, domainDetails, 
					dns, digest, timestamp);
			Snapshot snapshot = new Snapshot(image, metadata);
			reply.setRequest(request);
			reply.setSnapshot(snapshot);
			
			return reply;
		}

		// Flag to stop the thread
		public void setStop() {
			stop = true;
		}

		public String getStatus() {
			ToStringHelper helper = MoreObjects.toStringHelper(this)
					.add("Worker_ID", this.tid);
			for (Message.Status status : Message.Status.values()) {
				helper.add(status.name(), counters.get(status));
			}
			return helper.toString();
		}
	}
}
