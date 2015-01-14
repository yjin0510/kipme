/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.keepme.data.storage;

import org.apache.commons.lang.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import redis.clients.jedis.Jedis;

import com.infoxu.app.keepme.data.DataUtil;
import com.infoxu.app.keepme.data.Message;
import com.infoxu.app.keepme.data.storage.StorageFactory.StorageType;
import com.infoxu.app.keepme.util.ServiceProperty;

/**
 * Wrapper for Redis access APIs
 * Cache storage should access database storage if it does not contain the key
 * @author yujin
 *
 */
class CacheStorage implements Storage {
	private static final Logger logger = LogManager.getLogger(CacheStorage.class);
	private Jedis jedis = null;
	private static final String REDIS_SERVER = ServiceProperty.getInstance()
				.getProperty("redis.server.address", "localhost");
	private static final int REDIS_PORT = Integer.parseInt(ServiceProperty.getInstance()
				.getProperty("redis.server.port", "6379"));
	private Storage dbStorage = null;
	
	/**
	 * Get message by index Id
	 * The message may not exist in cache, we retrieve from database (if exists) and then add to cache
	 */
	public Message get(long id) {
		byte[] key = SerializationUtils.serialize(DataUtil.getKeyFromId(id));
		byte[] val = jedis.get(key);
		Message message = null;
		if (val != null) {
			logger.debug("Found message with key " + id + " in cache.");
			message = (Message) SerializationUtils.deserialize(val);
		} else {
			// Try database
			logger.debug("Look in db for key " + id);
			message = dbStorage.get(id);
			if (message != null) {
				logger.debug("Found message with key " + id + " in db, add to cache.");
				// Insert into cache
				this.put(id, message);
			}
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
		dbStorage = StorageFactory.getInstance(StorageType.DATABASE);
		dbStorage.init();
	}

	public void close() {
		jedis.close();
		dbStorage.close();
	}

	public void delete(long id) {
		byte[] key = SerializationUtils.serialize(DataUtil.getKeyFromId(id));
		jedis.del(key);
	}

}
