/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.keepme.queue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.QueueingConsumer;

/**
 * @author yujin
 *
 */
public class BaseMessageQueueSender<T extends Serializable> implements MessageQueueSender<T> {	
	private Connection connection = null;
	private Channel channel = null;
	private MessageQueueType type = null;
	private QueueingConsumer consumer = null;
	private QueueingConsumer.Delivery delivery = null;

	public BaseMessageQueueSender(ConnectionFactory factory, MessageQueueType type) throws IOException {
		connection = factory.newConnection();
		channel = connection.createChannel();
		this.type = type;
		channel.queueDeclare(type.getqName(), true, false, false, null);
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

	public void send(T t) throws IOException {
		ByteArrayOutputStream boOut = new ByteArrayOutputStream(65536);
		ObjectOutputStream out = new ObjectOutputStream(boOut);
		out.writeObject(t);
		byte[] message = boOut.toByteArray();
		channel.basicPublish("", type.getqName(), MessageProperties.PERSISTENT_BASIC, message);
	}
}
