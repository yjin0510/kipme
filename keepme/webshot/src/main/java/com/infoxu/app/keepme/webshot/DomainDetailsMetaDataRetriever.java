/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.keepme.webshot;

import java.net.InetAddress;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.whois.WhoisClient;

import com.infoxu.app.keepme.util.ServiceProperty;

/**
 * @author yujin
 *
 */
final class DomainDetailsMetaDataRetriever extends BaseMetaDataRetriever {
	private static final Log logger = LogFactory.getLog(DomainDetailsMetaDataRetriever.class);
	
	// Regex variables for finding the right DNS registrar
	private static Pattern pattern;
	private Matcher matcher;
	
	private static final String WHOIS_SERVER_PATTERN = "Whois Server:\\s(.*)";
	private static final String WHOIS_SERVER = ServiceProperty.getInstance().
			getProperty("webshot.whois.server", WhoisClient.DEFAULT_HOST);
	static {
		pattern = Pattern.compile(WHOIS_SERVER_PATTERN);
	}
	
	@Override
	protected String process(URL url) {
		return getDetailSimple(url);
	}
	
	private String getDetailSimple(URL url) {
		String result = "";
		WhoisClient whois = new WhoisClient();
		try {
			String ip = InetAddress.getByName(url.getHost()).getHostAddress();
			whois.connect(WHOIS_SERVER);
			result = whois.query(ip);
		} catch (Exception e) {
			logger.error("Unable to retrieve DNS information for URL: " + url + ", " + e.getMessage());
			result = "Failed to retrieve DNS information for URL: " + url.toString();
		}
		return result;
	}

	private String getDetailComplex(URL url) {
		StringBuilder result = new StringBuilder("");
		 
		WhoisClient whois = new WhoisClient();
		try {
 
		  whois.connect(WhoisClient.DEFAULT_HOST);
 
		  String whoisData1 = whois.query("=" + url.getHost());
 
		  // append first result
		  result.append(whoisData1);
		  whois.disconnect();
 
		  // Get the correct whois server
		  matcher = pattern.matcher(whoisData1);
		  String whoisServerUrl = "";
		  // get last whois server
		  while (matcher.find()) {
			  whoisServerUrl = matcher.group(1);
		  }
		  
		  String whoisData2 = "";
		  if (!whoisServerUrl.equals("")) {
			  whois = new WhoisClient();
			
			  // whois -h whois.markmonitor.com google.com
			  whois.connect(whoisServerUrl);
			  whoisData2 = whois.query(url.getHost());	
 
			  // append 2nd result
			  result.append(whoisData2);
		  }
 
		} catch (Exception e) {
			logger.error("Unable to retrieve DNS information for URL: " + url + ", " + e.getMessage());
			return "Failed to retrieve DNS information for URL: " + url.toString();
		}
 
		return result.toString();
	}

	@Override
	protected void cleanup() {
		// TODO Auto-generated method stub

	}

}
