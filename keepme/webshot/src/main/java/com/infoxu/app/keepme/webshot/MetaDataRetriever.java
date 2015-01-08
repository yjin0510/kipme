/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.keepme.webshot;

import java.net.URL;

/**
 * Metadata is retrieved upon the success of snapshot retrieval.
 * It also leverages certain information that already exists in snapshot
 * @author yujin
 *
 */
interface MetaDataRetriever {
	public String getMetaData(URL url);
}
