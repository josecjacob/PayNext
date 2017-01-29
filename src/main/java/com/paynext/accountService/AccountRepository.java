package com.paynext.accountService;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface AccountRepository extends JpaRepository<Account, String> {

	void delete(Account entity);

	@Transactional(readOnly = true)
	boolean exists(String accountId);

	@Transactional(readOnly = true)
	public Account findByAccountId(String accountId);

	@Transactional(readOnly = true)
	List<Account> findByUserName(String userName);

	@Transactional(readOnly = true)
	List<Account> findByAccountHolderName(String accountHolderName);

	@Transactional(readOnly = true)
	@Cacheable("accounts")
	List<Account> findAll() throws DataAccessException;
}
