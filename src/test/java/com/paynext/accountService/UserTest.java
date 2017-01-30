package com.paynext.accountService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class UserTest {

	private User classUnderTest;

	@Before
	public void setUp() {
		classUnderTest = new User("testuser", "testPassword", "regular");
	}

	@Test
	public void testGetUserName() {
		assertEquals("testuser", classUnderTest.getUserName());
	}

	@Test
	public void testGetAccountType() {
		assertEquals("regular", classUnderTest.getAccountType());
	}

	@Test
	public void testGetPassword() {
		assertEquals("fd5cb51bafd60f6fdbedde6e62c473da6f247db271633e15919bab78a02ee9eb", classUnderTest.getPassword());
	}

	@Test
	public void testPasswordGeneration() {
		classUnderTest = new User("testuser", null, "regular");
		assertFalse(classUnderTest.getPassword().isEmpty());
	}

	@Test(expected = NullPointerException.class)
	public void testUserNameInvalidDueToNullString() {
		classUnderTest = new User(null, "testPassword", "regular");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testUserNameInvalidDueToEmptyString() {
		classUnderTest = new User("", "testPassword", "regular");
	}

	@Test(expected = NullPointerException.class)
	public void testAccountTypeInvalidDueToNullString() {
		classUnderTest = new User("testuser", "testPassword", null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAccountTypeInvalidDueToEmptyString() {
		classUnderTest = new User("testuser", "testPassword", "");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPasswordInvalidDueToEmptyString() {
		classUnderTest = new User("testuser", "", "regular");
	}
}