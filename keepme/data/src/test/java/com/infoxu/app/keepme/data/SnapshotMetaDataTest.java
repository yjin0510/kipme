package com.infoxu.app.keepme.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import junit.framework.Assert;

import org.testng.annotations.Test;

public class SnapshotMetaDataTest {
	private final SnapshotMetaData data = new SnapshotMetaData(
			"source", "url", "ip", "domainDetails", "dns", "digest", 1000L);
	@Test
	public void testClone() {		
		SnapshotMetaData data2 = new SnapshotMetaData(data);
		Assert.assertEquals(data.getSource(), data2.getSource());
		Assert.assertEquals(data.getUrl(), data2.getUrl());
		Assert.assertEquals(data.getIp(), data2.getIp());
		Assert.assertEquals(data.getDomainDetails(), data2.getDomainDetails());
		Assert.assertEquals(data.getDigest(), data2.getDigest());
		Assert.assertEquals(data.getTimestamp(), data2.getTimestamp());
		Assert.assertFalse(data == data2);
	}
	
	@Test
	public void testSerialization() throws IOException, ClassNotFoundException {
		// Write out
		ByteArrayOutputStream baOut = new ByteArrayOutputStream(1024);		
		ObjectOutputStream out = new ObjectOutputStream(baOut);
		out.writeObject(data);
		out.close();
		// Read in
		ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(baOut.toByteArray()));
		SnapshotMetaData data2 = (SnapshotMetaData) in.readObject();
		in.close();
		Assert.assertEquals(data.getSource(), data2.getSource());
		Assert.assertEquals(data.getUrl(), data2.getUrl());
		Assert.assertEquals(data.getIp(), data2.getIp());
		Assert.assertEquals(data.getDomainDetails(), data2.getDomainDetails());
		Assert.assertEquals(data.getDigest(), data2.getDigest());
		Assert.assertEquals(data.getTimestamp(), data2.getTimestamp());
		Assert.assertFalse(data == data2);
	}
}
