package com.paynext.accountService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

public class AccountTest {

	private Account classUnderTest;

	@Before
	public void setUp() {
		classUnderTest = new Account("1234567890AB", "Test User", "testuser", "testPassword", new BigDecimal(10));
	}

	@Test
	public void testGetAccountId() {
		assertEquals("1234567890AB", classUnderTest.getAccountId());
	}

	@Test
	public void testGetAccountHolderName() {
		assertEquals("Test User", classUnderTest.getAccountHolderName());
	}

	@Test
	public void testGetUserName() {
		assertEquals("testuser", classUnderTest.getUserName());
	}

	@Test
	public void testGetPassword() {
		assertEquals("testPassword", classUnderTest.getPassword());
	}

	@Test
	public void testPasswordGeneration() {
		classUnderTest = new Account("1234567890AB", "Test User", "testuser", null, new BigDecimal(10));
		assertFalse(classUnderTest.getPassword().isEmpty());
	}

	@Test
	public void testGetBalance() {
		assertEquals("10", classUnderTest.getBalance());
	}

	@Test(expected = NullPointerException.class)
	public void testAccountHolderNameInvalidDueToNullString() {
		classUnderTest = new Account("1234567890AB", null, "testuser", "testPassword", new BigDecimal(10));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAccountHolderNameInvalidDueToEmptyString() {
		classUnderTest = new Account("1234567890AB", " ", "testuser", "testPassword", new BigDecimal(10));
	}

	@Test(expected = NullPointerException.class)
	public void testUserNameInvalidDueToNullString() {
		classUnderTest = new Account("1234567890AB", "Test User", null, "testPassword", new BigDecimal(10));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUserNameInvalidDueToEmptyString() {
		classUnderTest = new Account("1234567890AB", "Test User", " ", "testPassword", new BigDecimal(10));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPasswordInvalidDueToEmptyString() {
		classUnderTest = new Account("1234567890AB", "Test User", "testuser", " ", new BigDecimal(10));
	}

	@Test
	public void testInitialBalanceWithLargeNumber() {
		classUnderTest = new Account("1234567890AB", "Test User", "testuser", "testPassword",
				new BigDecimal("100000000000000000000000000000000000000000000000"));
		assertEquals("100000000000000000000000000000000000000000000000", classUnderTest.getBalance());
	}

	@Test
	public void testWithDefaultBalance() {
		classUnderTest = new Account("1234567890AB", "Test User", "testuser", "testPassword", null);
		assertEquals("100", classUnderTest.getBalance());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInitialBalanceInvalidDueToNegativeBalance() {
		classUnderTest = new Account("1234567890AB", "Test User", "testuser", "testPassword", new BigDecimal(-1));
	}
}