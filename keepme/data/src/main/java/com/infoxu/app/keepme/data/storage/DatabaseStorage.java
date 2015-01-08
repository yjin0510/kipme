/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.keepme.data.storage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.infoxu.app.keepme.data.DataUtil;
import com.infoxu.app.keepme.data.Message;
import com.infoxu.app.keepme.data.hibernate.HibernateUtil;
import com.infoxu.app.keepme.data.hibernate.SnapshotRecord;

/**
 * @author yujin
 *
 */
class DatabaseStorage implements Storage {
	private static final Log logger = LogFactory.getLog(DatabaseStorage.class);
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
		return DataUtil.getMessageFromSnapshotRecord(ssr);
	}

	// Insert an item into database
	public void put(long id, Message message) {
		if (session == null) {
			throw new RuntimeException("Hibernate session has not been established yet.");
		}
		
		// Construct database record
		SnapshotRecord record = DataUtil.getSnapshotRecordFromMessage(message);
		
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();  
        Session session = sessionFactory.openSession();  
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
			sessionFactory.close();
		} catch (Exception e) {
			logger.error("Failed to close Hibernate session: " + e.getMessage());
		} finally {
			session = null;
			sessionFactory = null;
		}
	}
}
