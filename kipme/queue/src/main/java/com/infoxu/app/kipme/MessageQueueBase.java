/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.kipme;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * @author yujin
 *
 */
public class MessageQueueBase<T extends Serializable> implements MessageQueue<T> {	
	private Connection connection = null;
	private Channel channel = null;
	private MessageQueueType type = null;
	private QueueingConsumer consumer = null;

	public MessageQueueBase(ConnectionFactory factory, MessageQueueType type) throws IOException {
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
		channel.basicPublish("", type.getqName(), MessageProperties.PERSISTENT_TEXT_PLAIN, message);
	}


	public T getNext() throws IOException, ShutdownSignalException, 
					ConsumerCancelledException, InterruptedException, 
					ClassNotFoundException {
		if (consumer == null) {
			// Lazy initialization
			channel.basicQos(1); // Each time only pick one task
			consumer = new QueueingConsumer(channel);
		    channel.basicConsume(type.getqName(), false, consumer);
		}
		
		QueueingConsumer.Delivery delivery = consumer.nextDelivery();
	    byte[] message = delivery.getBody();
	    ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(message));
	    T t = (T) in.readObject();
	    return t;
	}
}
