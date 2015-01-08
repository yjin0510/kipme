/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.keepme.webshot;

import java.net.URL;

/**
 * Define basic function in metadata retrievers
 * @author yujin
 *
 */
abstract class BaseMetaDataRetriever implements MetaDataRetriever {	
	public String getMetaData(URL url) {
		String data = process(url);
		cleanup();
		return data;
	}
		
	protected abstract String process(URL url);
	
	protected abstract void cleanup();
}
