package com.paynext.accountService;

import java.math.BigDecimal;

import com.google.common.base.Preconditions;

public class Account {

	private String accountId;
	private String accountHolderName;
	private String userName;
	private String password;
	private BigDecimal balance;

	public Account(String accountId, String accountHolderName, String userName, String password,
			BigDecimal initialBalance) {
		Preconditions.checkNotNull(accountHolderName, "The account holder name cannot be null.");
		Preconditions.checkNotNull(userName, "The username name cannot be null.");
		Preconditions.checkNotNull(password, "The password name cannot be null.");
		Preconditions.checkNotNull(initialBalance, "The initial balance name cannot be null.");

		Preconditions.checkArgument(!accountHolderName.trim().isEmpty(), "The account holder name cannot be empty.");
		Preconditions.checkArgument(!userName.trim().isEmpty(), "The username name cannot be empty.");
		Preconditions.checkArgument(!password.trim().isEmpty(), "The password name cannot be empty.");
		Preconditions.checkArgument(initialBalance.signum() != -1, "The initial balance name cannot be negative.");

		this.accountId = accountId;
		this.accountHolderName = accountHolderName;
		this.balance = initialBalance;
		this.userName = userName;
		this.password = password;
	}

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
}