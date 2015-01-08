/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.keepme.data;

import java.io.Serializable;

import com.google.common.base.MoreObjects;

/**
 * Message to be transmitted between services
 * Mutable, but the member variables status and snapshot are both immutable
 * @author yujin
 *
 */
public final class Message implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -243678920366513587L;

	// Status of message
	public enum Status {
		SUCCESS_IMAGE_METADATA (1), // Both image and metadata acquired
		FAIL_IMAGE (2), // Failed to retrieve image
		INCOMPLETE_METADATA (3), // Complete image but incomplete metadata
		INVALID_URL (4); // URL invalid, should not appear since we sanity check first before submitting URL to the queue
		
		private int id;
		Status(int id) {
			this.id = id;
		}
		
		public int getId() {
			return id;
		}
	}
	
	private Status status = Status.SUCCESS_IMAGE_METADATA;
	private Snapshot snapshot = null;
	private RequestMessage request = null;
	
	public Message() {
		
	}
	
	/**
	 * @param status
	 * @param snapshot
	 * @param request
	 */
	public Message(Status status, Snapshot snapshot, RequestMessage request) {
		this.setStatus(status);
		this.setSnapshot(snapshot);
		this.setRequest(request);
	}
	
	// Copy constructor in place import org.testng.collections.Objects;
	public Message(Message toClone) {
		this.setStatus(toClone.getStatus());
		this.setSnapshot(toClone.getSnapshot());
		this.setRequest(new RequestMessage(toClone.getRequest()));
		// FIXME: May not need to call copy constructor since the request will be serialized soon
	}

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * @return the snapshot
	 */
	public Snapshot getSnapshot() {
		return snapshot;
	}

	/**
	 * @param snapshot the snapshot to set
	 */
	public void setSnapshot(Snapshot snapshot) {
		this.snapshot = snapshot;
	}
	
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("status", status)
			.add("snapshot", snapshot)
			.add("request_message", request)
			.toString();
	}

	public RequestMessage getRequest() {
		return request;
	}

	public void setRequest(RequestMessage request) {
		this.request = request;
	}
}
