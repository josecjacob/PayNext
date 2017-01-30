package com.paynext.accountService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class SessionTest {

	private Session classUnderTest;
	private long sessionStartedTime;

	@Before
	public void setUp() throws InterruptedException {
		this.sessionStartedTime = System.nanoTime();
		Thread.sleep(50);
		classUnderTest = new Session("123456", "aTestUser", 120);
	}

	@Test
	public void testGetSessionId() {
		assertEquals("123456", classUnderTest.getSessionId());
	}

	@Test
	public void testGetUserName() {
		assertEquals("aTestUser", classUnderTest.getUserName());
	}

	@Test
	public void testGetTimeout() {
		assertEquals(new Integer(120), classUnderTest.getTimeout());
	}

	@Test
	public void testIsExpired() {
		assertFalse(classUnderTest.isExpired());
	}

	@Test
	public void testSetExpired() {
		classUnderTest.setExpired(true);
		assertTrue(classUnderTest.isExpired());
	}

	@Test
	public void testGetLastAccessTime() throws InterruptedException {
		assertTrue(classUnderTest.getLastAccessTime() > this.sessionStartedTime);
		Thread.sleep(50);
		assertTrue(classUnderTest.getLastAccessTime() < System.nanoTime());
	}

	@Test
	public void testSetLastAccessTime() {
		long lastAccessTime = System.nanoTime();
		classUnderTest.setLastAccessTime(lastAccessTime);
		assertEquals(new Long(lastAccessTime), classUnderTest.getLastAccessTime());
	}
}