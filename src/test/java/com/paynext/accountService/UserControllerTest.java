/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.paynext.accountService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = Application.class)
@Transactional
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testLogin() throws Exception {

		this.mockMvc
				.perform(get("/createAccount").param("accountHolderName", "Test User").param("userName", "testuser")
						.param("password", "testpassword").param("initialBalance", "20"))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.accountId").value("3YAI6S2YE49N"))
				.andExpect(jsonPath("$.accountHolderName").value("Test User"))
				.andExpect(jsonPath("$.userName").value("testuser")).andExpect(jsonPath("$.balance").value("20"))
				.andExpect(jsonPath("$.tombstoned").value(false));

		MvcResult result = this.mockMvc
				.perform(get("/login").param("userName", "testuser").param("password",
						"9f735e0df9a1ddc702bf0a1a7b83033f9f7153a00c29de82cedadc9957289b05"))
				.andDo(print()).andExpect(status().isOk()).andReturn();

		String sessionId = result.getResponse().getContentAsString();
		assertEquals(64, sessionId.length());

		this.mockMvc.perform(get("/getSessionsOfUser").param("userName", "testuser")).andDo(print())
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].sessionId").value(sessionId))
				.andExpect(jsonPath("$[0].userName").value("testuser"))
				.andExpect(jsonPath("$[0].timeout").value(new Integer(10 * 60 * 60).toString()))
				.andExpect(jsonPath("$[0].expired").value(false));
	}

	@Test
	public void testLogout() throws Exception {

		this.mockMvc
				.perform(get("/createAccount").param("accountHolderName", "Test User").param("userName", "testuser")
						.param("password", "testpassword").param("initialBalance", "20"))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.accountId").value("3YAI6S2YE49N"))
				.andExpect(jsonPath("$.accountHolderName").value("Test User"))
				.andExpect(jsonPath("$.userName").value("testuser")).andExpect(jsonPath("$.balance").value("20"))
				.andExpect(jsonPath("$.tombstoned").value(false));

		MvcResult result = this.mockMvc
				.perform(get("/login").param("userName", "testuser").param("password",
						"9f735e0df9a1ddc702bf0a1a7b83033f9f7153a00c29de82cedadc9957289b05"))
				.andDo(print()).andExpect(status().isOk()).andReturn();

		String sessionId = result.getResponse().getContentAsString();

		this.mockMvc.perform(get("/logout").param("sessionId", sessionId)).andDo(print()).andExpect(status().isOk());

		result = this.mockMvc.perform(get("/hasSessionExpired").param("sessionId", sessionId)).andDo(print())
				.andExpect(status().isOk()).andReturn();

		assertTrue(new Boolean(result.getResponse().getContentAsString()));

		this.mockMvc.perform(get("/getSessionsOfUser").param("userName", "testuser")).andDo(print())
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].sessionId").value(sessionId))
				.andExpect(jsonPath("$[0].userName").value("testuser"))
				.andExpect(jsonPath("$[0].timeout").value(new Integer(10 * 60 * 60).toString()))
				.andExpect(jsonPath("$[0].expired").value(true));
	}

	@Test
	public void testGetAllCurrentSessions() throws Exception {
		this.mockMvc
				.perform(get("/createAccount").param("accountHolderName", "Test User").param("userName", "testuser")
						.param("password", "testpassword").param("initialBalance", "20"))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.accountId").value("3YAI6S2YE49N"))
				.andExpect(jsonPath("$.accountHolderName").value("Test User"))
				.andExpect(jsonPath("$.userName").value("testuser")).andExpect(jsonPath("$.balance").value("20"))
				.andExpect(jsonPath("$.tombstoned").value(false));

		MvcResult result = this.mockMvc
				.perform(get("/login").param("userName", "testuser").param("password",
						"9f735e0df9a1ddc702bf0a1a7b83033f9f7153a00c29de82cedadc9957289b05"))
				.andDo(print()).andExpect(status().isOk()).andReturn();
		String sessionId = result.getResponse().getContentAsString();

		this.mockMvc.perform(get("/getAllCurrentSessions")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].sessionId").value(sessionId))
				.andExpect(jsonPath("$[0].userName").value("testuser"))
				.andExpect(jsonPath("$[0].timeout").value(new Integer(10 * 60 * 60).toString()))
				.andExpect(jsonPath("$[0].expired").value(false));

		this.mockMvc.perform(get("/logout").param("sessionId", sessionId)).andDo(print()).andExpect(status().isOk());

		result = this.mockMvc.perform(get("/getAllCurrentSessions")).andDo(print()).andExpect(status().isOk())
				.andReturn();

		String allSessions = result.getResponse().getContentAsString();
		assertEquals("[]", allSessions);
	}

	@SuppressWarnings("serial")
	@Test
	public void testTouchSession() throws Exception {

		this.mockMvc
				.perform(get("/createAccount").param("accountHolderName", "Test User").param("userName", "testuser")
						.param("password", "testpassword").param("initialBalance", "20"))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.accountId").value("3YAI6S2YE49N"))
				.andExpect(jsonPath("$.accountHolderName").value("Test User"))
				.andExpect(jsonPath("$.userName").value("testuser")).andExpect(jsonPath("$.balance").value("20"))
				.andExpect(jsonPath("$.tombstoned").value(false));

		MvcResult result = this.mockMvc
				.perform(get("/login").param("userName", "testuser").param("password",
						"9f735e0df9a1ddc702bf0a1a7b83033f9f7153a00c29de82cedadc9957289b05"))
				.andDo(print()).andExpect(status().isOk()).andReturn();

		String sessionId = result.getResponse().getContentAsString();

		result = this.mockMvc.perform(get("/getSessionsOfUser").param("userName", "testuser")).andDo(print())
				.andExpect(status().isOk()).andReturn();
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setLongSerializationPolicy(LongSerializationPolicy.STRING);
		Gson gson = gsonBuilder.create();
		List<Map<String, String>> allSessions = gson.fromJson(result.getResponse().getContentAsString(),
				new TypeToken<ArrayList<HashMap<String, String>>>() {
				}.getType());
		Object lastAccessTimeObject = allSessions.get(0).get("lastAccessTime");
		assertTrue(lastAccessTimeObject instanceof String);
		Long lastAccessTimeBeforeCallingTouchSession = Long.parseLong((String) lastAccessTimeObject);

		Thread.sleep(50);
		this.mockMvc.perform(get("/touchSession").param("sessionId", sessionId)).andDo(print())
				.andExpect(status().isOk());

		result = this.mockMvc.perform(get("/getSessionsOfUser").param("userName", "testuser")).andDo(print())
				.andExpect(status().isOk()).andReturn();

		allSessions = gson.fromJson(result.getResponse().getContentAsString(),
				new TypeToken<ArrayList<HashMap<String, String>>>() {
				}.getType());
		lastAccessTimeObject = allSessions.get(0).get("lastAccessTime");
		assertTrue(lastAccessTimeObject instanceof String);
		Long lastAccessTimeAfterCallingTouchSession = Long.parseLong((String) lastAccessTimeObject);

		assertTrue(lastAccessTimeBeforeCallingTouchSession < lastAccessTimeAfterCallingTouchSession);
		assertTrue(lastAccessTimeAfterCallingTouchSession < System.nanoTime());
	}

	@Test
	public void testExpireSessionsOfUser() throws Exception {

		this.mockMvc
				.perform(get("/createAccount").param("accountHolderName", "Test User").param("userName", "testuser")
						.param("password", "testpassword").param("initialBalance", "20"))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.accountId").value("3YAI6S2YE49N"))
				.andExpect(jsonPath("$.accountHolderName").value("Test User"))
				.andExpect(jsonPath("$.userName").value("testuser")).andExpect(jsonPath("$.balance").value("20"))
				.andExpect(jsonPath("$.tombstoned").value(false));

		MvcResult result = this.mockMvc
				.perform(get("/login").param("userName", "testuser").param("password",
						"9f735e0df9a1ddc702bf0a1a7b83033f9f7153a00c29de82cedadc9957289b05"))
				.andDo(print()).andExpect(status().isOk()).andReturn();

		String sessionId = result.getResponse().getContentAsString();

		this.mockMvc.perform(get("/expireSessionsOfUser").param("userName", "testuser")).andDo(print())
				.andExpect(status().isOk());

		result = this.mockMvc.perform(get("/hasSessionExpired").param("sessionId", sessionId)).andDo(print())
				.andExpect(status().isOk()).andReturn();

		assertTrue(new Boolean(result.getResponse().getContentAsString()));

		this.mockMvc.perform(get("/getSessionsOfUser").param("userName", "testuser")).andDo(print())
				.andExpect(status().isOk()).andExpect(jsonPath("$[0].sessionId").value(sessionId))
				.andExpect(jsonPath("$[0].userName").value("testuser"))
				.andExpect(jsonPath("$[0].timeout").value(new Integer(10 * 60 * 60).toString()))
				.andExpect(jsonPath("$[0].expired").value(true));
	}
}