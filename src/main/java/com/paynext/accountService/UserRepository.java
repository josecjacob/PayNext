package com.paynext.accountService;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, String> {

	@Transactional(readOnly = true)
	boolean exists(String userName);

	@Transactional(readOnly = true)
	User findByUserName(String userName);

	@Transactional(readOnly = true)
	@Cacheable("users")
	List<User> findAll() throws DataAccessException;
}