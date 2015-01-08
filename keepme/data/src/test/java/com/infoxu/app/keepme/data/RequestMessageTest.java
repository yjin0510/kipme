package com.infoxu.app.keepme.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import junit.framework.Assert;

import org.testng.annotations.Test;

public class RequestMessageTest {
	private final long reqId = 1;
	private final String url = "url";
	private final long indexId = 2;
	private final long userId = 3;
	private final long expireTime = 4;
	
	private final RequestMessage message = 
			new RequestMessage(reqId, url, indexId, userId, expireTime);
	
	@Test
	public void testClone() {
		RequestMessage message2 = new RequestMessage(message);
		Assert.assertEquals(message.getReqId(), message2.getReqId());
		Assert.assertEquals(message.getUrl(), message2.getUrl());
		Assert.assertEquals(message.getIndexId(), message2.getIndexId());
		Assert.assertEquals(message.getUserId(), message2.getUserId());
		Assert.assertEquals(message.getExpireTime(), message2.getExpireTime());
	}
	
	@Test
	public void testSerialization() throws IOException, ClassNotFoundException {
		// Write out
		ByteArrayOutputStream baOut = new ByteArrayOutputStream(1024);		
		ObjectOutputStream out = new ObjectOutputStream(baOut);
		out.writeObject(message);
		out.close();
		// Read in
		ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(baOut.toByteArray()));
		RequestMessage message2 = (RequestMessage) in.readObject();
		in.close();

		Assert.assertFalse(message == message2);
		Assert.assertEquals(message.getReqId(), message2.getReqId());
		Assert.assertEquals(message.getUrl(), message2.getUrl());
		Assert.assertEquals(message.getIndexId(), message2.getIndexId());
		Assert.assertEquals(message.getUserId(), message2.getUserId());
		Assert.assertEquals(message.getExpireTime(), message2.getExpireTime());
	}
}
