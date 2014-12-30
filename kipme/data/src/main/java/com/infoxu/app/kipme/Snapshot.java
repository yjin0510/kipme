/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.kipme;

import java.io.IOException;
import java.io.Serializable;


/**
 * @author yujin
 *
 */
public final class Snapshot implements Serializable {
	/**
	 * Do not change versionUID
	 */
	private static final long serialVersionUID = 8960095451778851129L;
	
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
	
	private void writeObject(java.io.ObjectOutputStream out)
		     throws IOException {
		out.writeInt(this.image.length);
		out.write(this.image);
		out.writeObject(this.metaData);
	}
	
	private void readObject(java.io.ObjectInputStream in)
		     throws IOException, ClassNotFoundException {
		int size = in.readInt();
		this.image = new byte[size];
		in.read(this.image);
		this.metaData = (SnapshotMetaData) in.readObject();
	}
}
