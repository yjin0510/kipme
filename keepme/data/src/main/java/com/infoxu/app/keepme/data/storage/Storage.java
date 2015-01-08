/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.keepme.data.storage;

import com.infoxu.app.keepme.data.Message;

/**
 * @author yujin
 *
 */
public interface Storage {
	public Message get(long id);
	public void put(long id, Message message); // id, object, time-to-live
	public void init(); // initialize connection to storage
	public void close(); // close connection
}
