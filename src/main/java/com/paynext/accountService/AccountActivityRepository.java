package com.paynext.accountService;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface AccountActivityRepository extends JpaRepository<AccountActivity, Long> {

	void delete(AccountActivity entity);

	@Transactional(readOnly = true)
	List<AccountActivity> findByAccountId(String accountId);

	@Transactional(readOnly = true)
	@Cacheable("accountActivities")
	List<AccountActivity> findAll() throws DataAccessException;
}
