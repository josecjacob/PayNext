package com.paynext.accountService;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface AccountRepository extends JpaRepository<AccountEntity, String> {

	void delete(AccountEntity entity);

	@Transactional(readOnly = true)
	boolean exists(String accountId);

	@Transactional(readOnly = true)
	public AccountEntity findByAccountId(String accountId);

	@Transactional(readOnly = true)
	List<AccountEntity> findByUserName(String userName);

	@Transactional(readOnly = true)
	List<AccountEntity> findByAccountHolderName(String accountHolderName);

	@Transactional(readOnly = true)
	@Cacheable("accounts")
	List<AccountEntity> findAll() throws DataAccessException;
}
