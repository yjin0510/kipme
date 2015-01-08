/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.keepme.monitor;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.collect.Lists;

/**
 * An auditor that keeps reporting instances of registered Auditable objects
 * @author yujin
 *
 */
public class Auditor implements Runnable {
	private static final Log logger = LogFactory.getLog(Auditor.class);
	private static final int MONITOR_INTERVAL_MILLIS = 30000;
	private List<Auditable> list = Lists.newArrayList();
	
	public Auditor() {
		
	}
	
	public void run() {
		while (true) {
			for (Auditable obj : list) {
				logger.info(obj.getStatus());
			}
			try {
				Thread.sleep(MONITOR_INTERVAL_MILLIS); // Every 5 minutes
			} catch (InterruptedException e) {
				logger.error("Auditor thread experienced exception: " + e.getMessage());
			}
		}
	}

	public void register(Auditable obj) {
		list.add(obj);
	}
}
