package com.infoxu.app.keepme.webshot;

import org.testng.annotations.Test;

import com.infoxu.app.keepme.data.RequestMessage;
import com.infoxu.app.keepme.data.User;
import com.infoxu.app.keepme.queue.MessageQueueFactory;
import com.infoxu.app.keepme.queue.MessageQueueSender;
import com.infoxu.app.keepme.queue.MessageQueueType;
import com.infoxu.app.keepme.util.Util;

public class WebShotServiceIntegrationTest {
	private static final String[] urls = new String[] {
		"www.cs.umn.edu",
//		"http://www.yahoo.com",
//		"http://www.mitbbs.com",
	};
	
	@Test(enabled = false)
	public void testWebShotService() throws Exception {
		WebShotService wss = new WebShotService();
		wss.init(null);
		wss.start();
		
//		RequestMessage reqMsg = new RequestMessage();
		MessageQueueSender<RequestMessage> sendQ = MessageQueueFactory.getInstance()
				.getMessageQueueSender(MessageQueueType.REQUEST_QUEUE);
//		for (int i = 0; i < urls.length; i++) {
//			reqMsg.setReqId(i);
//			reqMsg.setUrl(urls[i]);
//			sendQ.send(reqMsg);
//		}
		long expireTime = (System.currentTimeMillis() / 1000) + 86400; // one day to expire
		RequestMessage request = new RequestMessage(Util.generateLongId(), 
				urls[0], Util.generateLongId(), User.ANONYMOUS_USER_ID, expireTime);
		sendQ.send(request);
		System.out.println("Request send with indexId " + request.getIndexId());
		
		Thread.sleep(60000);
		wss.stop();
		wss.destroy();
	}
	
	
}
