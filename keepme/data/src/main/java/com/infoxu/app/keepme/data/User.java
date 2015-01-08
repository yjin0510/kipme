/**
 * Copyright (C) 2015 Infoxu Inc. All Rights Reserved.
 * Proprietary and confidential
 */
package com.infoxu.app.keepme.data;

import java.io.Serializable;

/**
 * @author yujin
 *
 */
public final class User implements Serializable {
	public static final long ANONYMOUS_USER_ID = -1;
	private static final long serialVersionUID = 8519701972550418925L;
	// User versionId for version control
	private static transient int versionId = 1;
	private long userId;
	private String userName;
	private String email;
	private String password; // or password hash
	
	/**
	 * Constructor
	 * @param userId
	 * @param userName
	 * @param email
	 * @param password
	 */
	public User(long userId, String userName, String email, String password) {
		this.userId = userId;
		this.userName = userName;
		this.email = email;
		this.password = password;
	}

	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
