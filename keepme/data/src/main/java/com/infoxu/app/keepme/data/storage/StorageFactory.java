/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.keepme.data.storage;

/**
 * @author yujin
 *
 */
public final class StorageFactory {
	public enum StorageType {
		CACHE,
		DATABASE,
	}
	public static Storage getInstance(StorageType type) {
		if (type == StorageType.CACHE) {
			return new CacheStorage();
		} else if (type == StorageType.DATABASE) {
			return new DatabaseStorage();
		} else {
			throw new UnsupportedOperationException("Unsupported storage type " + type);
		}
	}
}
