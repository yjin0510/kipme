/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.keepme.webshot;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
	private static Logger logger = LogManager.getLogger(SeleniumSnapshotRetriever.class);
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
			driver = new FirefoxDriver(firefox, null);
		} else {
			throw new UnsupportedOperationException("Selenium snapshot retriever type " + 
					type + " unsupported");
		}
	}
	
	public byte[] getSnapshot(URL request) {
		// set a timeout to load a page, avoid driver freezing
		driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS); 
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
			logger.error("Fail to close WebDriver: ", e);
		} finally {
			driver = null;
			firefox = null;
		}
	}
}
