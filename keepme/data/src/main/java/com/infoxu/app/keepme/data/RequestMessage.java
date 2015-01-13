/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.keepme.data;

import java.io.Serializable;

import com.google.common.base.MoreObjects;

/**
 * @author yujin
 *
 */
public class RequestMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4707764609889401726L;
	private long reqId; // unique identifier of the session, will not be saved into database
	private String url; // request url
	private long indexId; 	// index id, used for retrieving an existing snapshot 
							// or as the primary key for the new snapshot
	private long userId = User.ANONYMOUS_USER_ID; // unique identifier for the user
	private long expireTime; // expiration timestamp, in milliseconds
	
	public RequestMessage() {
		
	}
	
	/**
	 * 
	 * @param reqId
	 * @param url
	 * @param indexId
	 * @param userId
	 * @param expireTime
	 */
	public RequestMessage(long reqId, String url, long indexId, long userId, long expireTime) {
		this.setReqId(reqId);
		this.setUrl(url);
		this.setIndexId(indexId);
		this.setUserId(userId);
		this.setExpireTime(expireTime);
	}
	
	public RequestMessage(RequestMessage toClone) {
		this.setReqId(toClone.getReqId());
		this.setUrl(toClone.getUrl());
		this.setIndexId(toClone.getIndexId());
		this.setUserId(toClone.getUserId());
		this.setExpireTime(toClone.getExpireTime());
	}

	public long getReqId() {
		return reqId;
	}

	public void setReqId(long reqId) {
		this.reqId = reqId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getIndexId() {
		return indexId;
	}

	public void setIndexId(long indexId) {
		this.indexId = indexId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}
	
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("reqId", reqId)
				.add("url", url)
				.add("indexId", indexId)
				.add("userId", userId)
				.add("expireTime", expireTime)
				.toString();
	}
}
