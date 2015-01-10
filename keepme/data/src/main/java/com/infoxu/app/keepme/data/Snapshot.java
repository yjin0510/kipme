/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.keepme.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.google.common.base.MoreObjects;


/**
 * @author yujin
 *
 */
public final class Snapshot implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6541863775597624126L;

	// PNG byte binary
	private byte[] image;
	
	// Meta data
	private SnapshotMetaData metaData;
	
	/**
	 * Constructor
	 * @param image
	 * @param metaData
	 */
	public Snapshot(byte[] image, SnapshotMetaData metaData) {
		this.image = image.clone();
		this.metaData = metaData;
	}
	
	public Snapshot(Snapshot toClone) {
		this.image = toClone.getImage(); // a new array returned
		this.metaData = toClone.getMetaData(); // MetaData is immutable, okay to share
	}

	/**
	 * image is mutable, make a defensive copy
	 * @return the image
	 */
	public byte[] getImage() {
		return image.clone();
	}

	/**
	 * MetaData is immutable
	 * @return the metaData
	 */
	public SnapshotMetaData getMetaData() {
		return metaData;
	}
	
	/**
	 * For serialization, we compress the Snapshot object and store the
	 * compressed object in database and cache
	 * @param out
	 * @throws IOException
	 */
	private void writeObject(java.io.ObjectOutputStream out)
		     throws IOException {
		// Compress the data into a byte array
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		GZIPOutputStream gzout = new GZIPOutputStream(baos);
		ObjectOutputStream objOut = new ObjectOutputStream(gzout);
		objOut.writeInt(this.image.length);
		objOut.write(this.image);
		objOut.writeObject(this.metaData);
		objOut.close();
		// Serialize the byte array
		byte[] data = baos.toByteArray();
		out.writeInt(data.length);
		out.write(data);
	}
	
	private void readObject(java.io.ObjectInputStream in)
		     throws IOException, ClassNotFoundException {
		// Read the compressed data trunk
		int dataSize = in.readInt();
		byte[] data = new byte[dataSize];
		in.read(data);
		// Decompress to restore the data
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		GZIPInputStream gzin = new GZIPInputStream(bais);
		ObjectInputStream objIn = new ObjectInputStream(gzin);
		int size = objIn.readInt();
		this.image = new byte[size];
		objIn.read(this.image);
		this.metaData = (SnapshotMetaData) objIn.readObject();
		objIn.close();
	}
	
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("image size", image.length)
				.add("metadata", metaData)
				.toString();
	}
}
