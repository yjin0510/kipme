/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.keepme.util;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.infoxu.app.keepme.monitor.Auditable;

/**
 * Base worker that every service worker should extend from
 * @author yujin
 *
 */
public class BaseWorker extends Thread implements Auditable {
	private static Logger logger = LogManager.getLogger(BaseWorker.class);
	
	/**
	 * Auditor thread will keep pulling status information from each worker
	 */
	public String getStatus() {
		return this.getName() + " return status";
	}
	
	@Override
	public void run() {
		
		logger.debug(this.getName() + " runs at " + new Date());
		try {
			Thread.sleep((long) Math.random() * 1000L);
		} catch (InterruptedException e) {
			logger.error(e);
		}
	}
}
