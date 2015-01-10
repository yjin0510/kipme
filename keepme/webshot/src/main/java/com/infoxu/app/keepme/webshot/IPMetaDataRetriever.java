/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.keepme.webshot;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author yujin
 *
 */
final class IPMetaDataRetriever extends BaseMetaDataRetriever {
	private static Logger logger = LogManager.getLogger(IPMetaDataRetriever.class);
	@Override
	protected String process(URL url) {
		InetAddress[] machines;
		StringBuilder sb = new StringBuilder();
		try {
			machines = InetAddress.getAllByName(url.getHost());
			for(InetAddress address : machines){
				  sb.append(address.getHostAddress() + ",");
			}
		} catch (UnknownHostException e) {
			logger.error("Unable to get IP addresses for URL: " + url + ", " + e.getMessage());
			return "Unable to get IP addresses for URL: " + url;
		}
		
		return sb.toString();
	}

	@Override
	protected void cleanup() {
		// Do nothing
	}
}
