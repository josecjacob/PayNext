package com.paynext.accountService;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;

import com.google.common.base.Preconditions;

@SuppressWarnings("serial")
@Entity
@Table(name = "accountactivity")
public class AccountActivity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String accountId;

	@Temporal(TemporalType.TIME)
	private Date timestamp;

	@Digits(integer = 20, fraction = 2)
	@Column(name = "amount")
	private BigDecimal amount;

	private boolean credit;

	public Long getId() {
		return id;
	}

	public String getAccountId() {
		return accountId;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getAmount() {
		return amount.toString();
	}

	public boolean isCredit() {
		return credit;
	}

	public AccountActivity(String accountId, boolean credit, BigDecimal amount) {
		Preconditions.checkNotNull(accountId, "The account id cannot be null.");
		Preconditions.checkNotNull(amount, "The amount cannot be null.");

		Preconditions.checkArgument(!accountId.trim().isEmpty(), "The accountId cannot be empty.");

		this.accountId = accountId;
		Preconditions.checkArgument(amount.signum() != -1, "The amount cannot be negative.");
		this.amount = amount;
		this.timestamp = new Date();
		this.credit = credit;
	}

	AccountActivity() { // jpa only
	}
}
