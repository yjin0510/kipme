package com.infoxu.app.keepme.data.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.testng.annotations.Test;

import com.infoxu.app.keepme.data.Message;
import com.infoxu.app.keepme.data.DataUtil;

public class SnapshotRecordIntegrationTest {
	private Message message = DataUtil.randMessage();
	private SnapshotRecord record = null;
	
	@Test
	public void test() {
		testCreate();
		testQuery();
		testDelete();
	}
	
	private void testCreate() {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();  
        Session session = sessionFactory.openSession();  
        session.beginTransaction();  

        record = new SnapshotRecord();
        record.setId(message.getRequest().getIndexId());
        record.setUserId(message.getRequest().getUserId());
        record.setExpireTime(new Date(message.getRequest().getExpireTime()));
        record.setSnapshot(message.getSnapshot());
        record.setStatus(message.getStatus().getId());
        record.setUrl(message.getRequest().getUrl());
        
        session.save(record);  
        session.getTransaction().commit();  
          
        session.close(); 
	}
	
	private void testQuery() {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();  
        Session session = sessionFactory.openSession();  
          
        List<SnapshotRecord> rs = session.createQuery("from SnapshotRecord").list();  
          
        session.close();  
        for (SnapshotRecord r : rs) {  
            System.out.println(r.toString());  
        }
        
        System.out.println("original data: " + record.toString());
	}
	
	private void testDelete() {
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();  
        Session session = sessionFactory.openSession();  
        session.beginTransaction();  
          
        SnapshotRecord r = (SnapshotRecord) session.get(SnapshotRecord.class, record.getId());  
 
        session.delete(r);  
        session.getTransaction().commit();  
          
        session.close();  
	}
}
