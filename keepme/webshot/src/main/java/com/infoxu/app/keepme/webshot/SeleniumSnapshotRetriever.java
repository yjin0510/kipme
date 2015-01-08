/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.keepme.webshot;

import java.net.URL;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Retrieve image using 
 * @author yujin
 *
 */
final class SeleniumSnapshotRetriever {
	private static Log logger = LogFactory.getLog(SeleniumSnapshotRetriever.class);
	public enum SSRType {
		SELENIUM_FIREFOX,
		SELENIUM_CHROME,
		SELENIUM_IPHONE,
		SELENIUM_IPAD,
		SELENIUM_ANDROID,
		SELENIUM_IE,
	}
	
	private FirefoxBinary firefox = null;
	private WebDriver driver = null;
	private byte[] image = null;
	
	public SeleniumSnapshotRetriever(SSRType type) {
		if (type == SSRType.SELENIUM_FIREFOX) {
			firefox = new FirefoxBinary();
//			firefox.setEnvironmentProperty("DISPLAY", "-1");
			driver = new FirefoxDriver(firefox, null);
		} else {
			throw new UnsupportedOperationException("Selenium snapshot retriever type " + 
					type + " unsupported");
		}
	}
	
	public byte[] getSnapshot(URL request) {
		driver.get(request.toString());
		image = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
		return image;
	}
	
	// Note the three meta data retriever below depends on the success of snapshot
	// Get web page source
	public String getSource() {
		return driver.getPageSource();
	}
	
	// Get URL, may not be the same as the given URL (e.g., redirection)
	public String getCurrentURL() {
		return driver.getCurrentUrl();
	}
	
	// Get message digest for the image
	public String getDigest() {
		return DigestUtils.sha1Hex(image); 
	}
	
	public void close() {
		try {
			driver.close();
		} catch (Exception e) {
			logger.error("Fail to close WebDriver: " + e.getMessage());
		} finally {
			driver = null;
			firefox = null;
		}
	}
}
