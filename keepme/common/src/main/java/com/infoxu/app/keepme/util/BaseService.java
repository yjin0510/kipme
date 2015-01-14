/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.keepme.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.infoxu.app.keepme.monitor.Auditor;

/**
 * Parent class for all the services. Can be executed as a Daemon
 * @author yujin
 *
 */
public class BaseService implements Daemon {
	private static Logger logger = LogManager.getLogger(BaseService.class);
	protected int numWorkers = 1;
	protected String serviceName = "BaseService";
	protected ExecutorService executor = null;
	protected Class<? extends BaseWorker> workerClazz = BaseWorker.class;
	
	/**
	 * In subclass constructor, we should set numWorkers, serviceName and workerClazz
	 */
	public BaseService() {
		executor = Executors.newFixedThreadPool(numWorkers + 1);
		
	}
	
	public void destroy() {
		executor.shutdown();
		logger.info(serviceName + " terminated.");
	}


	public void init(DaemonContext arg0) throws DaemonInitException, Exception {
		Auditor auditor = new Auditor();
		executor.execute(auditor);
		
		// Create and register workers
		for (int i = 0; i < numWorkers; i++) {
			BaseWorker worker = workerClazz.newInstance();
			worker.setName(serviceName + "-worker-" + i);
			executor.execute(worker);
			auditor.register(worker);
		}
		logger.info("WebShotService initialized.");
	}

	
	public void start() throws Exception {

	}


	public void stop() throws Exception {

	}


	public static void main(String[] args) {

	}

}
