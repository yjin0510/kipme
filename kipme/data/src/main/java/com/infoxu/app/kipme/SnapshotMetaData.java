/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.kipme;

import java.io.Serializable;

/**
 * Metadata associated with each snapshot, immutable
 * @author yujin
 *
 */
public class SnapshotMetaData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6910639779713663935L;
	
	private final String source; // Page source code
	private final String url;
	private final String ip;
	private final String domainDetails; // Owner, host, etc., from whois lookup
	private final String dns; // From nslookup results
	private final String digest; // Image digest from SHA-1 256
	private final long timestamp; // Time when the snapshot is taken
	
	public SnapshotMetaData(String source, String url, 
			String ip, String domainDetails, 
			String dns, String digest, long timestamp) {
		this.source = source;
		this.url = url;
		this.ip = ip;
		this.domainDetails = domainDetails;
		this.dns = dns;
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
		this.dns = toClone.getDns();
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
	 * @return the dns
	 */
	public String getDns() {
		return dns;
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
}
