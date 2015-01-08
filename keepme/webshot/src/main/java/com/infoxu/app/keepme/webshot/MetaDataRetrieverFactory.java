/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.keepme.webshot;

/**
 * @author yujin
 *
 */
public final class MetaDataRetrieverFactory {
	public enum MDRType {
		IP,
		DOMAIN_DETAILS,
	}
	
	public static MetaDataRetriever getMetaDataRetriever(MDRType type) {
		if (type == MDRType.IP) {
			return new IPMetaDataRetriever();
		} else if (type == MDRType.DOMAIN_DETAILS) {
			return new DomainDetailsMetaDataRetriever();
		} else {
			throw new UnsupportedOperationException("Metadata retriever type: " + type + " unsupported");
		}
	}
}
