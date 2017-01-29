package com.paynext.accountService;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;

import com.google.common.base.Preconditions;

@SuppressWarnings("serial")
@Entity
@Table(name = "vets")
public class Account implements Serializable {

	@Id
	private String accountId;

	public String password;

	public String userName;

	private String accountHolderName;

	@Digits(integer = 20, fraction = 2)
	@Column(name = "balance")
	private BigDecimal balance;

	public String getAccountId() {
		return accountId;
	}

	public String getAccountHolderName() {
		return accountHolderName;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public String getBalance() {
		return balance.toString();
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Account(String accountId, String accountHolderName, String userName, String password,
			BigDecimal initialBalance) {
		Preconditions.checkNotNull(accountHolderName, "The account holder name cannot be null.");
		Preconditions.checkNotNull(userName, "The username name cannot be null.");

		Preconditions.checkArgument(!accountHolderName.trim().isEmpty(), "The account holder name cannot be empty.");
		Preconditions.checkArgument(!userName.trim().isEmpty(), "The username name cannot be empty.");

		this.accountId = accountId;
		this.accountHolderName = accountHolderName;
		this.userName = userName;

		if (initialBalance == null) {
			this.balance = new BigDecimal(100);
		} else {
			Preconditions.checkArgument(initialBalance.signum() != -1, "The initial balance name cannot be negative.");
			this.balance = initialBalance;
		}

		if (password != null) {
			Preconditions.checkArgument(!password.trim().isEmpty(), "The password name cannot be empty.");
			this.password = password;
		} else {
			SecureRandom random = new SecureRandom();
			this.password = new BigInteger(130, random).toString(32).substring(0, 8);
		}
	}

	Account() { // jpa only
	}
}
