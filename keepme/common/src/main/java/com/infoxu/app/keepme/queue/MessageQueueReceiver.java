/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.keepme.queue;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author yujin
 *
 */
public interface MessageQueueReceiver <T extends Serializable>{
	public T getNext() throws Exception;
//	public void acknowledge() throws IOException;
	public void close() throws IOException; 
}
