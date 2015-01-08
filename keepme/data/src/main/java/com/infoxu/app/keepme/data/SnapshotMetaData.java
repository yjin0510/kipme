/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.keepme.data;

import java.io.Serializable;
import java.util.List;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;

/**
 * Metadata associated with each snapshot, immutable
 * @author yujin
 *
 */
public final class SnapshotMetaData implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4862780961015728150L;
	private final String source; // Page source code
	private final String url;
	private final String ip;
	private final String domainDetails; // Owner, host, etc., from whois lookup
	private final String digest; // Image digest from SHA-1 256
	private final long timestamp; // Time when the snapshot is taken
	
	/**
	 * 
	 * @param source
	 * @param url
	 * @param ip
	 * @param domainDetails
	 * @param dns
	 * @param digest
	 * @param timestamp
	 */
	public SnapshotMetaData(String source, String url, 
			String ip, String domainDetails, 
			String dns, String digest, long timestamp) {
		this.source = source;
		this.url = url;
		this.ip = ip;
		this.domainDetails = domainDetails;
		this.digest = digest;
		this.timestamp = timestamp;
	}
	
	/**
	 * Copy constructor as a replacement for the clone method
	 * @param toClone
	 */
	public SnapshotMetaData(SnapshotMetaData toClone) {
		this.source = toClone.getSource();
		this.url = toClone.getUrl();
		this.ip = toClone.getIp();
		this.domainDetails = toClone.getDomainDetails();
		this.digest = toClone.getDigest();
		this.timestamp = toClone.getTimestamp();
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @return the domainDetails
	 */
	public String getDomainDetails() {
		return domainDetails;
	}

	/**
	 * @return the digest
	 */
	public String getDigest() {
		return digest;
	}

	/**
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}
	
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("source size", source.length())
				.add("url", url)
				.add("ip", ip)
				.add("domainDetails size", domainDetails.length())
				.add("digest", digest)
				.add("timestamp", timestamp)
				.toString();
	}
}
