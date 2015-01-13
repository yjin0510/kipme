/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.keepme.data.storage;

import org.apache.commons.lang.SerializationUtils;

import redis.clients.jedis.Jedis;

import com.infoxu.app.keepme.data.DataUtil;
import com.infoxu.app.keepme.data.Message;
import com.infoxu.app.keepme.util.ServiceProperty;

/**
 * Wrapper for Redis access APIs
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
		byte[] key = SerializationUtils.serialize(DataUtil.getKeyFromId(id));
		byte[] val = jedis.get(key);
		Message message = null;
		if (val != null) {
			message = (Message) SerializationUtils.deserialize(val);
		}
		return message;
	}

	public void put(long id, Message message) {
		byte[] key = SerializationUtils.serialize(DataUtil.getKeyFromId(id));
		byte[] val = SerializationUtils.serialize(message);
		jedis.set(key, val);
	}

	public void init() {
		jedis = new Jedis(REDIS_SERVER, REDIS_PORT);
		
	}

	public void close() {
		// TODO Auto-generated method stub
		
	}

	public void delete(long id) {
		byte[] key = SerializationUtils.serialize(DataUtil.getKeyFromId(id));
		jedis.del(key);
	}

}
