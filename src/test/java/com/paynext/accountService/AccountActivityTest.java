package com.paynext.accountService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class AccountActivityTest {

	private AccountActivity classUnderTest;

	@Before
	public void setUp() {
		classUnderTest = new AccountActivity("1234567890AB", true, new BigDecimal(20));
	}

	@Test
	public void testGetAccountId() {
		assertEquals("1234567890AB", classUnderTest.getAccountId());
	}

	@Test
	public void testGetAmount() {
		assertEquals("20", classUnderTest.getAmount());
	}

	@Test
	public void testIsCredit() {
		assertTrue(classUnderTest.isCredit());
	}

	@Test
	public void testGetTimestamp() throws InterruptedException {
		Thread.sleep(100);
		assertTrue(classUnderTest.getTimestamp().before(new Date()));
	}

	@Test(expected = NullPointerException.class)
	public void testAccountHolderNameInvalidDueToNullString() {
		classUnderTest = new AccountActivity(null, true, new BigDecimal(20));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAccountHolderNameInvalidDueToEmptyString() {
		classUnderTest = new AccountActivity("", true, new BigDecimal(20));
	}

	@Test(expected = NullPointerException.class)
	public void testTransferAmountInvalidDueToNullString() {
		classUnderTest = new AccountActivity("1234567890AB", true, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTransferAmountInvalidDueToNegativeNumber() {
		classUnderTest = new AccountActivity("1234567890AB", true, new BigDecimal(-20));
	}

	@Test
	public void testInitialBalanceWithLargeNumber() {
		classUnderTest = new AccountActivity("1234567890AB", false,
				new BigDecimal("100000000000000000000000000000000000000000000000"));
		assertEquals("100000000000000000000000000000000000000000000000", classUnderTest.getAmount());
	}
}