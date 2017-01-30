package com.paynext.accountService;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;

@RestController
@Transactional
public class UserController {

	private int defaultTimeout;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SessionRepository sessionRepository;

	void setDefaultTimeout(int defaultTimeout) {
		this.defaultTimeout = defaultTimeout;
	}

	@RequestMapping("/login")
	@Transactional
	public String login(@RequestParam(value = "userName") String userName,
			@RequestParam(value = "password", required = false) String password) {
		validateRequestParameters(userName, password);

		User aUser = userRepository.findByUserName(userName);
		Preconditions.checkArgument(aUser != null, "The user name: " + userName + " does not exists.");
		Preconditions.checkArgument(aUser.getPassword().equals(password), "");

		String sessionId = generateNewSessionId(userName);
		Session newSession = new Session(sessionId, userName, defaultTimeout);
		sessionRepository.save(newSession);

		return sessionId;
	}

	private void validateRequestParameters(String userName, String password) {
		Preconditions.checkNotNull(userName, "The username name cannot be null.");
		Preconditions.checkNotNull(password, "The password cannot be null.");

		Preconditions.checkArgument(!userName.trim().isEmpty(), "The userName cannot be empty.");
		Preconditions.checkArgument(!password.trim().isEmpty(), "The password cannot be empty.");
	}

	private String generateNewSessionId(String userName) {
		SecureRandom random = new SecureRandom();
		String sessionIdString = new BigInteger(130, random).toString(32);
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("Could not get a SHA-256 message digest instance.");
		}

		return new BigInteger(1, digest.digest(sessionIdString.getBytes())).toString(16);
	}

	@RequestMapping("/logout")
	@Transactional
	public void logout(@RequestParam(value = "sessionId") String sessionId) {

		Preconditions.checkNotNull(sessionId, "The sessionId cannot be null.");
		Preconditions.checkArgument(!sessionId.trim().isEmpty(), "The sessionId cannot be empty.");

		Session currentSession = sessionRepository.findBySessionId(sessionId);
		Preconditions.checkArgument(currentSession != null, "The session id: " + currentSession + " does not exist.");

		currentSession.setLastAccessTime(System.nanoTime());
		currentSession.setExpired(true);

		sessionRepository.save(currentSession);
	}

	@RequestMapping("/touchSession")
	@Transactional
	public void touchSession(@RequestParam(value = "sessionId") String sessionId) {

		Preconditions.checkNotNull(sessionId, "The sessionId cannot be null.");
		Preconditions.checkArgument(!sessionId.trim().isEmpty(), "The sessionId cannot be empty.");

		Session currentSession = sessionRepository.findBySessionId(sessionId);
		Preconditions.checkArgument(currentSession != null, "The session id: " + currentSession + " does not exist.");

		currentSession.setLastAccessTime(System.nanoTime());

		sessionRepository.save(currentSession);
	}

	@RequestMapping("/expireSessionsOfUser")
	@Transactional
	public void expireSessionsOfUser(@RequestParam(value = "userName") String userName) {

		Preconditions.checkNotNull(userName, "The userName cannot be null.");
		Preconditions.checkArgument(!userName.trim().isEmpty(), "The userName cannot be empty.");

		List<Session> currentSessionsForUser = sessionRepository.findByUserName(userName);

		for (Session aSession : currentSessionsForUser) {
			aSession.setExpired(true);
		}

		sessionRepository.save(currentSessionsForUser);
	}

	@RequestMapping("/changePassword")
	@Transactional
	public void changePassword(@RequestParam(value = "userName") String userName,
			@RequestParam(value = "password") String password) {
		throw new UnsupportedOperationException("Not yet implemented.");
	}

	@ExceptionHandler
	void handleIllegalArgumentException(NullPointerException e, HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler
	void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}

	@ExceptionHandler
	void handleIllegalStateException(IllegalStateException e, HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}
}
