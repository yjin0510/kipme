/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.kipme;

/**
 * @author yujin
 *
 */
public enum MessageQueueType {
	REQUEST_QUEUE(1, "request_queue"),
	REPLY_QUEUE(2, "reply_queue"),
	INDEX_QUEUE(3, "index_queue");
	
	private final int qId;
	private final String qName;
	
	MessageQueueType(int qId, String qName) {
		this.qId = qId;
		this.qName = qName;
	}

	/**
	 * @return the qId
	 */
	public int getqId() {
		return qId;
	}

	/**
	 * @return the qName
	 */
	public String getqName() {
		return qName;
	}
}
