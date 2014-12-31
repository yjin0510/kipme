/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.kipme;

import java.io.IOException;
import java.io.Serializable;

import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * @author yujin
 *
 */
public interface MessageQueue <T extends Serializable>{
	public void send(T t) throws IOException;
	public T getNext() throws IOException, ShutdownSignalException, 
							ConsumerCancelledException, InterruptedException, 
							ClassNotFoundException;
	public void close();
}
