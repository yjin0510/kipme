/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.keepme.util;

import java.util.Random;

import org.apache.commons.validator.routines.UrlValidator;

/**
 * All the utility functions
 * @author yujin
 *
 */
public class Util {
	private static final Random rnd = new Random();
	private static final String[] schemes = {"http","https"};
	private static final UrlValidator urlValidator = new UrlValidator(schemes, 
			UrlValidator.ALLOW_2_SLASHES + UrlValidator.NO_FRAGMENTS);
	static {
		rnd.setSeed(System.currentTimeMillis());
	}
	
	public static long generateLongId() {
		return Math.abs(rnd.nextLong());
	}
	
	public static String getValidURL(String reqUrl) {
		if (urlValidator.isValid(reqUrl)) {
			return reqUrl;
		} else if (urlValidator.isValid("http://" + reqUrl)) {
			return reqUrl;
		}
		return null;
	}
}
