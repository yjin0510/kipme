/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.keepme.data.storage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.infoxu.app.keepme.data.DataUtil;
import com.infoxu.app.keepme.data.Message;
import com.infoxu.app.keepme.data.hibernate.HibernateUtil;
import com.infoxu.app.keepme.data.hibernate.SnapshotRecord;

/**
 * Wrapper for Hibernate database access APIs
 * @author yujin
 *
 */
class DatabaseStorage implements Storage {
	private static final Logger logger = LogManager.getLogger(DatabaseStorage.class);
	private SessionFactory sessionFactory = null;
	private Session session = null;
	
	public Message get(long id) {
		if (session == null) {
			throw new RuntimeException("Hibernate session has not been established yet.");
		}
		
		SnapshotRecord ssr = null;
		try {
			ssr = (SnapshotRecord) session.get(SnapshotRecord.class, id);
		} catch (Exception e) {
			logger.error("Unable to retrieve SnapshotRecord for id " 
					+ id + " : " + e.getMessage());
		}
		if (ssr == null) {
			logger.debug("Failed to find key " + id + " in db.");
			return null;
		} else {
			logger.debug("Found key " + id + " in db.");
		}
		return DataUtil.getMessageFromSnapshotRecord(ssr);
	}

	// Insert an item into database
	public void put(long id, Message message) {
		// Construct database record
		SnapshotRecord record = DataUtil.getSnapshotRecordFromMessage(message);
		
		if (session == null) {
			throw new RuntimeException("Hibernate session has not been established yet.");
		}
        session.beginTransaction();  

        session.save(record);  
        session.getTransaction().commit();  
	}

	public void init() {
		try {
			sessionFactory = HibernateUtil.getSessionFactory(); 
			session = sessionFactory.openSession();
		} catch (Exception e) {
			logger.error("Failed to establish Hibernate session: " + e.getMessage());
			if (session != null) {
				session.close();
				session = null;
			}
			if (sessionFactory != null) {
				sessionFactory.close();
				sessionFactory = null;
			}
		}
	}

	public void close() {
		try {
			session.close(); 
//			sessionFactory.close();
		} catch (Exception e) {
			logger.error("Failed to close Hibernate session: " + e.getMessage());
		} finally {
			session = null;
			sessionFactory = null;
		}
	}

	public void delete(long id) {
		if (session == null) {
			throw new RuntimeException("Hibernate session has not been established yet.");
		}
		
		session.beginTransaction();
		SnapshotRecord ssr = null;
		try {
			ssr = (SnapshotRecord) session.get(SnapshotRecord.class, id);
		} catch (Exception e) {
			logger.error("Unable to retrieve SnapshotRecord for id " 
					+ id + " : " + e.getMessage());
		}
		session.delete(ssr);
		session.getTransaction().commit();
	}
}
