/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.keepme.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author yujin
 *
 */
public final class ServiceProperty {
	private static Log logger = LogFactory.getLog(ServiceProperty.class);
	private static Properties prop = null;
	private static ServiceProperty sp = null;
	
	private ServiceProperty() throws FileNotFoundException, IOException {
	}
	
	public synchronized static ServiceProperty getInstance() {
		if (sp == null) {
			try {
				sp = new ServiceProperty();
				prop = new Properties();
				FileInputStream fin = new FileInputStream("/home/yujin/workspace/infoxu/keepme/properties/service.properties");
				prop.load(fin);
				fin.close();
			} catch (Exception e) {
				logger.error("Failed to load property file: " + e.getMessage());
				prop = null;
			}
		}
		return sp;
	}
	
	public String getProperty(String name, String defaultValue) {
		if (prop == null) {
			return defaultValue;
		} else {
			return prop.getProperty(name, defaultValue);
		}
	}
}
