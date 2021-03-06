package com.infoxu.app.keepme.data.storage;

import org.apache.commons.lang.SerializationUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.infoxu.app.keepme.data.DataUtil;
import com.infoxu.app.keepme.data.Message;
import com.infoxu.app.keepme.data.storage.StorageFactory.StorageType;


/**
 * Test persistence APIs: cache storage and database storage
 * @author yujin
 *
 */
public class StorageFactoryTest {
	private Message message = DataUtil.randMessage();
	long id = message.getRequest().getIndexId();
	
	private void testSetAndGet(Storage storage) {
		storage.init();
		storage.put(id, message);
		Message message2 = storage.get(id);
		Assert.assertEquals(SerializationUtils.serialize(message.getSnapshot()), SerializationUtils.serialize(message2.getSnapshot()));
//		Assert.assertEquals(message.getRequest().getExpireTime(), message2.getRequest().getExpireTime());
		Assert.assertEquals(message.getRequest().getIndexId(), message2.getRequest().getIndexId());
		Assert.assertEquals(message.getRequest().getUrl(), message2.getRequest().getUrl());
		Assert.assertEquals(message.getRequest().getUserId(), message2.getRequest().getUserId());
		
		storage.delete(id);
		Message message3 = storage.get(id);
		Assert.assertNull(message3);
		storage.close();
	}
	
	private void testCacheMiss() {
		Storage db = StorageFactory.getInstance(StorageType.DATABASE);
		Storage cache = StorageFactory.getInstance(StorageType.CACHE);
		db.init();
		cache.init();
		db.put(id,  message);
		Message message2 = cache.get(id);
		Message message3 = db.get(id);
		
		Assert.assertEquals(SerializationUtils.serialize(message3.getSnapshot()), SerializationUtils.serialize(message2.getSnapshot()));
//		Assert.assertEquals(message3.getRequest().getExpireTime(), message2.getRequest().getExpireTime());
		Assert.assertEquals(message3.getRequest().getIndexId(), message2.getRequest().getIndexId());
		Assert.assertEquals(message3.getRequest().getUrl(), message2.getRequest().getUrl());
		Assert.assertEquals(message3.getRequest().getUserId(), message2.getRequest().getUserId());
		
		db.delete(id);
		cache.delete(id);
		db.close();
		cache.close();
	}

	@Test
	public void testStorage() {
		testSetAndGet(StorageFactory.getInstance(StorageType.CACHE));
		testSetAndGet(StorageFactory.getInstance(StorageType.DATABASE));
		testCacheMiss();
	}
}
