package com.infoxu.app.keepme.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.testng.Assert;
import org.testng.annotations.Test;

public class SnapshotTest {
	private final SnapshotMetaData data = new SnapshotMetaData(
			"source", "url", "ip", "domainDetails", "dns", "digest", 1000L);
	private byte[] image = new byte[] {1, 2};
	private final Snapshot snapshot = new Snapshot(image, data);
	
	@Test
	public void testClone() {
		Snapshot snapshot2 = new Snapshot(snapshot);
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
		ByteArrayOutputStream baOut = new ByteArrayOutputStream(1024);		
		ObjectOutputStream out = new ObjectOutputStream(baOut);
		out.writeObject(snapshot);
		out.close();
		// Read in
		ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(baOut.toByteArray()));
		Snapshot snapshot2 = (Snapshot) in.readObject();
		SnapshotMetaData data2 = snapshot2.getMetaData();
		in.close();
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
