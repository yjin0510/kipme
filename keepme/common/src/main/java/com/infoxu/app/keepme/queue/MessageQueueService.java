/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.keepme.queue;

import java.io.IOException;

import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * @author yujin
 *
 */
public class MessageQueueService {
	public static void main(String[] args) throws IOException {
		MessageQueueSender<String> sendQ = MessageQueueFactory.getInstance()
				.getMessageQueueSender(MessageQueueType.REQUEST_QUEUE);
		MessageQueueReceiver<String> recvQ = MessageQueueFactory.getInstance()
				.getMessageQueueReceiver(MessageQueueType.REQUEST_QUEUE);
		sendQ.send("abc");
		sendQ.send("fgh");
		
		try {
			System.out.println(recvQ.getNext());
			System.out.println(recvQ.getNext());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			sendQ.close();
			recvQ.close();
		}
	}
}
