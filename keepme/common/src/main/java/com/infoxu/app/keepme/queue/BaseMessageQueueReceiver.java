/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.keepme.queue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * @author yujin
 *
 */
public final class BaseMessageQueueReceiver<T extends Serializable> implements MessageQueueReceiver<T> {
	private Connection connection = null;
	private Channel channel = null;
	private MessageQueueType type = null;
	private QueueingConsumer consumer = null;
	private QueueingConsumer.Delivery delivery = null;

	public BaseMessageQueueReceiver(ConnectionFactory factory, 
			MessageQueueType type) throws IOException {
		connection = factory.newConnection();
		channel = connection.createChannel();
		this.type = type;
		channel.queueDeclare(type.getqName(), true, false, false, null);
		channel.basicQos(1); // Each time only pick one task
		consumer = new QueueingConsumer(channel);
	    channel.basicConsume(type.getqName(), true, consumer);
	}	
	
	public void close() {
		try {
			channel.close();
			connection.close();
		} catch (IOException e) {
			System.err.println("Unable to close connection to RequestQueue properly");
		} finally {
			connection = null;
			channel = null;
		}
	}

	public T getNext() throws Exception {	
		delivery = consumer.nextDelivery();
	    byte[] message = delivery.getBody();
	    ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(message));
	    T t = (T) in.readObject();
	    return t;
	}
	
	// Use auto acknowledgement: may lose customer request
//	public void acknowledge() throws IOException {
//		channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
//	}
}
