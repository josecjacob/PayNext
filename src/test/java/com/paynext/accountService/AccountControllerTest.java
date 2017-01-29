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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = Application.class)
public class AccountControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testAccountCreation() throws Exception {

		this.mockMvc
				.perform(get("/createAccount").param("accountHolderName", "Test User").param("userName", "testuser")
						.param("password", "testpassword").param("initialBalance", "20"))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.accountId").value("3YAI6S2YE49N"))
				.andExpect(jsonPath("$.accountHolderName").value("Test User"))
				.andExpect(jsonPath("$.userName").value("testuser"))
				.andExpect(jsonPath("$.password").value("testpassword")).andExpect(jsonPath("$.balance").value("20"));
	}

	@Test
	public void testDuplicateAccount() throws Exception {

		this.mockMvc
				.perform(get("/createAccount").param("accountHolderName", "Test User 2").param("userName", "testuser2")
						.param("password", "testpassword2").param("initialBalance", "20"))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.accountId").value("H6VM3LXTCEAT"))
				.andExpect(jsonPath("$.accountHolderName").value("Test User 2"))
				.andExpect(jsonPath("$.userName").value("testuser2"))
				.andExpect(jsonPath("$.password").value("testpassword2")).andExpect(jsonPath("$.balance").value("20"));
		this.mockMvc
				.perform(get("/createAccount").param("accountHolderName", "Test User 2").param("userName", "testuser2")
						.param("password", "testpassword2").param("initialBalance", "20"))
				.andDo(print()).andExpect(status().is4xxClientError());
	}
}