package com.infoxu.app.keepme.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.infoxu.app.keepme.data.Message.Status;

public class MessageTest {
	private final SnapshotMetaData data = new SnapshotMetaData(
			"source", "url", "ip", "domainDetails", "dns", "digest", 1000L);
	private final byte[] image = new byte[] {1, 2};
	private final Snapshot snapshot = new Snapshot(image, data);
	private final Status status = Status.FAIL_IMAGE;
	private final RequestMessage request = new RequestMessage(1L, "req_url", 2L, 3L, 4L);
	private final Message message = new Message(status, snapshot, request);
	
	@Test
	public void testClone() {
		Message message2 = new Message(message);
		Status status2 = message2.getStatus();
		RequestMessage request2 = message2.getRequest();
		Assert.assertFalse(request == request2);
		Assert.assertEquals(request.getReqId(), request2.getReqId());
		
		Assert.assertEquals(status, status2);
		Snapshot snapshot2 = message2.getSnapshot();
		Assert.assertTrue(snapshot == snapshot2);
		
		SnapshotMetaData data2 = snapshot2.getMetaData();
		Assert.assertTrue(data == data2); // both snapshots point to the same MetaData
		
		byte[] image2 = snapshot2.getImage();
		Assert.assertEquals(image[0], image2[0]);
		Assert.assertEquals(image[1], image2[1]);
		Assert.assertFalse(image == image2);
	}
	
	@Test
	public void testSerialization() throws IOException, ClassNotFoundException {
		// Write out
		ByteArrayOutputStream baOut = new ByteArrayOutputStream(4096);		
		ObjectOutputStream out = new ObjectOutputStream(baOut);
		out.writeObject(message);
		out.close();
		// Read in
		ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(baOut.toByteArray()));
		Message message2 = (Message) in.readObject();
		in.close();

		Assert.assertFalse(message == message2);
		
		RequestMessage request2 = message2.getRequest();
		Status status2 = message2.getStatus();
		Snapshot snapshot2 = message2.getSnapshot();
		
		Assert.assertEquals(request.getReqId(), request2.getReqId());
		Assert.assertEquals(status, status2);
		Assert.assertFalse(snapshot == snapshot2); // A new snapshot generated after serialization
		
		SnapshotMetaData data2 = snapshot2.getMetaData();
		// A new object from deserialization
		Assert.assertFalse(snapshot == snapshot2);
		// Also a new SnapshotMetaData from deserialization
		Assert.assertFalse(data == data2);
		Assert.assertEquals(data.getSource(), data2.getSource());
		Assert.assertEquals(data.getUrl(), data2.getUrl());
		Assert.assertEquals(data.getIp(), data2.getIp());
		Assert.assertEquals(data.getDomainDetails(), data2.getDomainDetails());
		Assert.assertEquals(data.getDigest(), data2.getDigest());
		Assert.assertEquals(data.getTimestamp(), data2.getTimestamp());
		// Test image
		byte[] image2 = snapshot2.getImage();
		Assert.assertEquals(image[0], image2[0]);
		Assert.assertEquals(image[1], image2[1]);
	}
}
