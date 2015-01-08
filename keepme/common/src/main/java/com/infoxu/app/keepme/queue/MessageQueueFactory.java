/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.keepme.queue;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

import com.infoxu.app.keepme.util.ServiceProperty;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

/**
 * @author yujin
 *
 */
public class MessageQueueFactory {
    private static MessageQueueFactory mqf = null;
    
	private static ConnectionFactory factory = new ConnectionFactory();
    private static String host;
    
    // Singleton factory
    private MessageQueueFactory() {
    	host = ServiceProperty.getInstance().getProperty("host", "localhost");
    	factory.setHost(host);
    }
    
    public static synchronized MessageQueueFactory getInstance() {
    	if (mqf != null) {
    		return mqf;
    	} else {
    		return new MessageQueueFactory();
    	}
    }
    
	public <T extends Serializable> MessageQueueSender<T> getMessageQueueSender(MessageQueueType type) {
		if (factory == null) {
			throw new IllegalStateException("Need to create MessageQueueFactory before creating queue");
		} 
		
		try {
			return new BaseMessageQueueSender<T>(factory, type);
		} catch (IOException e) {
			System.err.println("Unable to create message queue for type: " + type);
			throw new IllegalStateException("Unable to create message queue for type: " + type);
		}
	}
	
	public <T extends Serializable> MessageQueueReceiver<T> getMessageQueueReceiver(MessageQueueType type) {
		if (factory == null) {
			throw new IllegalStateException("Need to create MessageQueueFactory before creating queue");
		} 
		
		try {
			return new BaseMessageQueueReceiver<T>(factory, type);
		} catch (IOException e) {
			System.err.println("Unable to create message queue for type: " + type);
			throw new IllegalStateException("Unable to create message queue for type: " + type);
		}
	}
}
