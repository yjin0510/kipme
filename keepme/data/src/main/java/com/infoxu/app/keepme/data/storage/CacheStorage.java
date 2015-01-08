/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.keepme.data.storage;

import redis.clients.jedis.Jedis;

import com.infoxu.app.keepme.data.DataUtil;
import com.infoxu.app.keepme.data.Message;
import com.infoxu.app.keepme.util.ServiceProperty;

/**
 * Main class for cache access
 * @author yujin
 *
 */
class CacheStorage implements Storage {
	private Jedis jedis = null;
	private static final String REDIS_SERVER = ServiceProperty.getInstance()
				.getProperty("redis.server.address", "localhost");
	private static final int REDIS_PORT = Integer.parseInt(ServiceProperty.getInstance()
				.getProperty("redis.server.port", "6379"));
	
	public Message get(long id) {
		// TODO Auto-generated method stub
//		return (Message) jedis.get(DataUtil.getKeyFromId(id));
		return null;
	}

	public void put(long id, Message message) {
		// TODO Auto-generated method stub

	}

	public void init() {
		jedis = new Jedis(REDIS_SERVER, REDIS_PORT);
		
	}

	public void close() {
		// TODO Auto-generated method stub
		
	}

}
