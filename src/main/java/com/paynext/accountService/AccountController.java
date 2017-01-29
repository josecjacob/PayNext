package com.paynext.accountService;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
public class AccountController {

	@Autowired
	private AccountRepository accountRepository;

	@RequestMapping("/createAccount")
	@Transactional
	public Account greeting(@RequestParam(value = "accountHolderName") String accountHolderName,
			@RequestParam(value = "userName") String userName,
			@RequestParam(value = "password", required = false) String password,
			@RequestParam(value = "initialBalance", required = false) String initialBalance) {
		validateRequestParameters(accountHolderName, userName, password, initialBalance);
		String accountId = generateAccountID(accountHolderName);

		Preconditions.checkArgument(!accountRepository.exists(accountId),
				"The account with holder name: " + accountHolderName + " already exists.");
		Preconditions.checkArgument(accountRepository.findByUserName(userName).size() == 0,
				"The given username: " + userName + " has been taken.");

		Account newAccount = null;
		if (initialBalance != null) {
			newAccount = new Account(accountId, accountHolderName, userName, password,
					new BigDecimal(initialBalance.replaceAll(",", "")));
		} else {
			newAccount = new Account(accountId, accountHolderName, userName, password, null);
		}
		accountRepository.save(newAccount);

		return newAccount;
	}

	private void validateRequestParameters(String accountHolderName, String userName, String password,
			String initialBalance) {
		Preconditions.checkNotNull(accountHolderName, "The account holder name cannot be null.");
		Preconditions.checkNotNull(userName, "The username name cannot be null.");
	}

	String generateAccountID(String accountHolderName) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("Could not get a SHA-256 message digest instance.");
		}

		byte[] hash;
		hash = digest.digest(accountHolderName.getBytes());
		return new BigInteger(1, hash).toString(36).toUpperCase().substring(0, 12);
	}

	@RequestMapping("/findAccountByUserName")
	@Transactional
	public Account findAccountByUserName(@RequestParam(value = "userName") String userName) {

		Preconditions.checkNotNull(userName, "The userName cannot be null.");

		List<Account> accountsByUserName = accountRepository.findByUserName(userName);
		if (accountsByUserName.size() == 0) {
			throw new IllegalArgumentException("");
		} else if (accountsByUserName.size() > 1) {
			throw new IllegalStateException("Got more than one account with the same user name: " + userName);
		} else {
			return accountsByUserName.get(0);
		}
	}

	@RequestMapping("/transferMoneyFromAnAccountToAnother")
	@Transactional
	public Account transferMoneyFromAnAccountToAnother(@RequestParam(value = "fromAccountId") String fromAccountId,
			@RequestParam(value = "toAccountId") String toAccountId,
			@RequestParam(value = "transferAmount") String transferAmount) {

		Preconditions.checkNotNull(fromAccountId, "The fromAccountId cannot be null.");
		Preconditions.checkNotNull(toAccountId, "The toAccountId cannot be null.");
		Preconditions.checkNotNull(transferAmount, "The transferAmount cannot be null.");
		String normalizedTransferAmount = transferAmount.replaceAll(",", "");

		Account fromAccount = accountRepository.findByAccountId(fromAccountId);
		Preconditions.checkArgument(fromAccount != null, "The account id: " + fromAccountId + " does not exist.");
		BigDecimal transferAmountD = new BigDecimal(normalizedTransferAmount);
		BigDecimal newBalance = new BigDecimal(fromAccount.getBalance()).subtract(transferAmountD);
		Preconditions.checkArgument(newBalance.signum() != -1, "Not enough balance (" + fromAccount.getBalance()
				+ ") in the from account to debit the following amount: " + normalizedTransferAmount);

		Account toAccount = accountRepository.findByAccountId(toAccountId);
		Preconditions.checkArgument(toAccount != null, "The account id: " + toAccountId + " does not exist.");

		fromAccount.setBalance(newBalance);
		toAccount.setBalance(new BigDecimal(toAccount.getBalance()).add(transferAmountD));

		accountRepository.save(fromAccount);
		accountRepository.save(toAccount);

		return fromAccount;
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
