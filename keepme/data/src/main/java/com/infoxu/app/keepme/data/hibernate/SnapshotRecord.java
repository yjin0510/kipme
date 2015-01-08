/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.keepme.data.hibernate;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;  
import javax.persistence.Id;
import javax.persistence.Table;  

import com.google.common.base.MoreObjects;
import com.infoxu.app.keepme.data.Snapshot;

/**
 * @author yujin
 *
 */
@Entity
@Table(name = "snapshot")
public class SnapshotRecord {
	private long id;
	private long userId;
	private Date expireTime;
	private String url;
	private Snapshot snapshot;
	private int status;
	
	public SnapshotRecord() {
		
	}
	
	public SnapshotRecord(long id, long userId, Date expireTime, String url, Snapshot snapshot, int status) {
		this.id = id;
		this.userId = userId;
		this.expireTime = expireTime;
		this.url = url;
		this.snapshot = snapshot;
		this.setStatus(status);
	}
	
	@Id
	@Column(name="id")
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	@Column(name="user_id")
	public long getUserId() {
		return userId;
	}
	
	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	@Column(name="expire_time")
	public Date getExpireTime() {
		return expireTime;
	}
	
	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	@Column(name="url")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(columnDefinition="longblob", name="snapshot_binary")
	public Snapshot getSnapshot() {
		return snapshot;
	}

	public void setSnapshot(Snapshot snapshot) {
		this.snapshot = snapshot;
	}

	@Column(name="status")
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("id", id)
			.add("userId", userId)
			.add("expireTime", expireTime)
			.add("status", status)
			.add("snapshot", snapshot)
			.toString();
	}
}
