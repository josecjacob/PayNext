package com.paynext.accountService;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;

import com.google.common.base.Preconditions;

@SuppressWarnings("serial")
@Entity
@Table(name = "account")
public class Account implements Serializable {

	@Id
	private String accountId;

	private String userName;

	private String accountHolderName;

	private boolean tombstoned;

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

	public String getBalance() {
		return balance.toString();
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public boolean isTombstoned() {
		return tombstoned;
	}

	public void setTombstoned(boolean tombstoned) {
		this.tombstoned = tombstoned;
	}

	public Account(String accountId, String accountHolderName, String userName, BigDecimal initialBalance) {
		Preconditions.checkNotNull(accountHolderName, "The account holder name cannot be null.");
		Preconditions.checkNotNull(userName, "The username cannot be null.");

		Preconditions.checkArgument(!accountHolderName.trim().isEmpty(), "The account holder name cannot be empty.");
		Preconditions.checkArgument(!userName.trim().isEmpty(), "The username cannot be empty.");

		this.accountId = accountId;
		this.accountHolderName = accountHolderName;
		this.userName = userName;
		this.tombstoned = false;

		if (initialBalance == null) {
			this.balance = new BigDecimal(100);
		} else {
			Preconditions.checkArgument(initialBalance.signum() != -1, "The initial balance cannot be negative.");
			this.balance = initialBalance;
		}
	}

	Account() { // jpa only
	}
}
