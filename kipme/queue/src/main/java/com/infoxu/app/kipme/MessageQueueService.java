/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.kipme;

import java.io.IOException;

import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * @author yujin
 *
 */
public class MessageQueueService {
	public static void main(String[] args) throws IOException {
		MessageQueue<String> queue = MessageQueueFactory.getInstance()
				.getMessageQueue(MessageQueueType.REQUEST_QUEUE);
		queue.send("abc");
		
		try {
			System.out.println(queue.getNext());
		} catch (ShutdownSignalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConsumerCancelledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		queue.close();
	}
}
