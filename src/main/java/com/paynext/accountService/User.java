package com.paynext.accountService;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.common.base.Preconditions;

@SuppressWarnings("serial")
@Entity
@Table(name = "user")
public class User implements Serializable {

	@Id
	private String userName;

	private String password;

	private String accountType;

	private String currentSessionId;

	private boolean disabled;

	public User(String userName, String password, String accountType) {
		Preconditions.checkNotNull(userName, "The username name cannot be null.");
		Preconditions.checkNotNull(accountType, "The accountType cannot be null.");

		Preconditions.checkArgument(!userName.trim().isEmpty(), "The account holder name cannot be empty.");
		if (password != null) {
			Preconditions.checkArgument(!password.trim().isEmpty(), "The password cannot be empty.");
			this.password = hashPassword(password);
		} else {
			SecureRandom random = new SecureRandom();
			String newPassword = new BigInteger(130, random).toString(32).substring(0, 8);
			// we need to find a way to get this password out security into the
			// client's hands ...
			this.password = hashPassword(newPassword);
		}
		Preconditions.checkArgument(!accountType.trim().isEmpty(), "The accountType cannot be empty.");

		this.userName = userName;
		this.accountType = accountType;
		this.disabled = false;
	}

	// we will only save the SHA 256 of the password in our DB ...
	// ideally we will need to salt it ...
	private String hashPassword(String password) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("Could not get a SHA-256 message digest instance.");
		}

		return new BigInteger(1, digest.digest(password.getBytes())).toString(16);
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public String getCurrentSessionId() {
		return currentSessionId;
	}

	public void setCurrentSessionId(String currentSessionId) {
		this.currentSessionId = currentSessionId;
	}

	User() { // jpa only
	}
}
