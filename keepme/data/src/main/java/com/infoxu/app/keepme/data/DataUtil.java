/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.keepme.data;

import java.util.Date;

import com.infoxu.app.keepme.data.Message.Status;
import com.infoxu.app.keepme.data.hibernate.SnapshotRecord;


/**
 * Utility class for test cases
 * @author yujin
 *
 */
public class DataUtil {
	public static SnapshotMetaData randSnapshotMetaData() {
		return new SnapshotMetaData("source", "url", 
				"ip", "domainDetails", 
				"dns", "digest", System.currentTimeMillis());
	}
	
	public static Snapshot randSnapshot() {
		return new Snapshot(new byte[] {1, 2, 3, 4}, randSnapshotMetaData());
	}
	
	public static RequestMessage randRequestMessage() {
		return new RequestMessage(1L, "req_url", 2L, 3L, 
				System.currentTimeMillis() + 3600 * 1000);
	}
	
	public static Message randMessage() {
		return new Message(Message.Status.SUCCESS_IMAGE_METADATA, 
				randSnapshot(), randRequestMessage());
	}
	
	public static Message getMessageFromSnapshotRecord(SnapshotRecord sr) {
		Message message = new Message();
		message.setSnapshot(sr.getSnapshot());
		// Status can be optimized 
		for (Status status : Message.Status.values()) {
			if (status.getId() == sr.getStatus()) {
				message.setStatus(status);
			}
		}
		RequestMessage request = new RequestMessage();
//		request.setReqId(reqId); // reqId is not materialized
		request.setUrl(sr.getUrl());
		request.setIndexId(sr.getId());
		request.setUserId(sr.getUserId());
		request.setExpireTime(sr.getExpireTime().getTime());
		message.setRequest(request);
		
		return message;
	}
	
	public static SnapshotRecord getSnapshotRecordFromMessage(Message message) {
		SnapshotRecord record = new SnapshotRecord();
		record.setId(message.getRequest().getIndexId());
        record.setUserId(message.getRequest().getUserId());
        record.setExpireTime(new Date(message.getRequest().getExpireTime()));
        record.setUrl(message.getRequest().getUrl());
        record.setSnapshot(message.getSnapshot());
        record.setStatus(message.getStatus().getId());
        return record;
	}
	
	public static long getIdFromKey(final String key) {
		return Long.parseLong(key, 36);
	}
	
	public static String getKeyFromId(final long id) {
		return Long.toString(id, 36);
	}
}
