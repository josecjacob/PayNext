package com.paynext.accountService;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.google.common.base.Preconditions;

@SuppressWarnings("serial")
@Entity
@Table(name = "session")
public class Session implements Serializable {

	@Id
	private String sessionId;

	@NotNull
	private String userName;

	@NotNull
	private Long lastAccessTime;

	@NotNull
	private Integer timeout;

	private boolean expired;

	public Session(String sessionId, String userName, Integer timeout) {
		Preconditions.checkNotNull(sessionId, "The sessionId cannot be null.");
		Preconditions.checkNotNull(userName, "The userName cannot be null.");
		Preconditions.checkNotNull(timeout, "The timeout cannot be null.");

		Preconditions.checkArgument(!sessionId.trim().isEmpty(), "The sessionId cannot be empty.");
		Preconditions.checkArgument(!userName.trim().isEmpty(), "The userName cannot be empty.");
		Preconditions.checkArgument(timeout > 0, "Timeout should be greater than 0 seconds; got " + timeout);

		this.sessionId = sessionId;
		this.userName = userName;
		this.timeout = timeout;
		this.lastAccessTime = System.nanoTime();
		this.expired = false;
	}

	public String getSessionId() {
		return this.sessionId;
	}

	public String getUserName() {
		return this.userName;
	}

	public Long getLastAccessTime() {
		return this.lastAccessTime;
	}

	public void setLastAccessTime(Long lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	Session() { // jpa only
	}
}
