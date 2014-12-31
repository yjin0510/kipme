/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.kipme;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

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
    	Properties prop = new Properties();
    	try {
			prop.load(new FileInputStream("/home/yujin/workspace/infoxu/kipme/properties/queue.properties"));
			host = prop.getProperty("host", "localhost");
			factory.setHost(host);
		} catch (IOException e) {
			host = null;
			factory = null;
			System.err.println("Failed to open property file");
			throw new IllegalStateException("Failed to open property file for MessageQueueFactory");
		}
    }
    
    public static synchronized MessageQueueFactory getInstance() {
    	if (mqf != null) {
    		return mqf;
    	} else {
    		return new MessageQueueFactory();
    	}
    }
    
	public static<T extends Serializable> MessageQueue<T> getMessageQueue(MessageQueueType type) {
		if (factory == null) {
			throw new IllegalStateException("Need to create MessageQueueFactory before creating queue");
		} 
		
		try {
			return new MessageQueueBase<T>(factory, type);
		} catch (IOException e) {
			System.err.println("Unable to create message queue for type: " + type);
			throw new IllegalStateException("Unable to create message queue for type: " + type);
		}
	}
}
