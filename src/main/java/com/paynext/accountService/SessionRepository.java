package com.paynext.accountService;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface SessionRepository extends JpaRepository<Session, String> {

	@Transactional(readOnly = true)
	boolean exists(String sessionId);

	@Transactional(readOnly = true)
	Session findBySessionId(String sessionId);

	@Transactional(readOnly = true)
	List<Session> findByUserName(String userName);

	@Transactional(readOnly = true)
	List<Session> findByExpired(Boolean expired);

	@Transactional(readOnly = true)
	@Cacheable("sessions")
	List<Session> findAll() throws DataAccessException;
}