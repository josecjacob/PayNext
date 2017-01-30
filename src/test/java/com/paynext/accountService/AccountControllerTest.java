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

import javax.transaction.Transactional;

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
@Transactional
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
				.andExpect(jsonPath("$.userName").value("testuser")).andExpect(jsonPath("$.balance").value("20"))
				.andExpect(jsonPath("$.tombstoned").value(false));
	}

	@Test
	public void testAccountCreationWithDefautlBalance() throws Exception {

		this.mockMvc
				.perform(get("/createAccount").param("accountHolderName", "Test User").param("userName", "testuser")
						.param("password", "testpassword"))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.accountId").value("3YAI6S2YE49N"))
				.andExpect(jsonPath("$.accountHolderName").value("Test User"))
				.andExpect(jsonPath("$.userName").value("testuser")).andExpect(jsonPath("$.balance").value("100"))
				.andExpect(jsonPath("$.tombstoned").value(false));
	}

	@Test
	public void testDuplicateAccount() throws Exception {

		this.mockMvc
				.perform(get("/createAccount").param("accountHolderName", "Test User").param("userName", "testuser")
						.param("password", "testpassword").param("initialBalance", "20"))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.accountId").value("3YAI6S2YE49N"))
				.andExpect(jsonPath("$.accountHolderName").value("Test User"))
				.andExpect(jsonPath("$.userName").value("testuser")).andExpect(jsonPath("$.balance").value("20"));
		this.mockMvc
				.perform(get("/createAccount").param("accountHolderName", "Test User").param("userName", "testuser")
						.param("password", "testpassword").param("initialBalance", "20"))
				.andDo(print()).andExpect(status().is4xxClientError());
	}

	@Test
	public void testUserNameExists() throws Exception {

		this.mockMvc
				.perform(get("/createAccount").param("accountHolderName", "Test User")
						.param("userName", "testuserDuplicate").param("password", "testpassword")
						.param("initialBalance", "20"))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.accountId").value("3YAI6S2YE49N"))
				.andExpect(jsonPath("$.accountHolderName").value("Test User"))
				.andExpect(jsonPath("$.userName").value("testuserDuplicate"))
				.andExpect(jsonPath("$.balance").value("20"));
		this.mockMvc.perform(
				get("/createAccount").param("accountHolderName", "Test User 2").param("userName", "testuserDuplicate")
						.param("password", "testpassword 2").param("initialBalance", "20"))
				.andDo(print()).andExpect(status().is4xxClientError());
	}

	@Test
	public void testFindAccountByUserName() throws Exception {

		this.mockMvc
				.perform(get("/createAccount").param("accountHolderName", "Test User").param("userName", "testuser")
						.param("password", "testpassword").param("initialBalance", "20"))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.accountId").value("3YAI6S2YE49N"))
				.andExpect(jsonPath("$.accountHolderName").value("Test User"))
				.andExpect(jsonPath("$.userName").value("testuser")).andExpect(jsonPath("$.balance").value("20"));

		this.mockMvc.perform(get("/findAccountByUserName").param("userName", "testuser")).andDo(print())
				.andExpect(status().isOk()).andExpect(jsonPath("$.accountId").value("3YAI6S2YE49N"))
				.andExpect(jsonPath("$.accountHolderName").value("Test User"))
				.andExpect(jsonPath("$.userName").value("testuser")).andExpect(jsonPath("$.balance").value("20"))
				.andExpect(jsonPath("$.tombstoned").value(false));
	}

	@Test
	public void testDeleteAccountByAccountID() throws Exception {

		this.mockMvc
				.perform(get("/createAccount").param("accountHolderName", "Test User").param("userName", "testuser")
						.param("password", "testpassword").param("initialBalance", "20"))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.accountId").value("3YAI6S2YE49N"))
				.andExpect(jsonPath("$.accountHolderName").value("Test User"))
				.andExpect(jsonPath("$.userName").value("testuser")).andExpect(jsonPath("$.balance").value("20"));

		this.mockMvc.perform(get("/deleteAccountByAccountID").param("accountId", "3YAI6S2YE49N")).andDo(print())
				.andExpect(status().isOk());

		this.mockMvc.perform(get("/findAccountByUserName").param("userName", "testuser")).andDo(print())
				.andExpect(status().isOk()).andExpect(jsonPath("$.accountId").value("3YAI6S2YE49N"))
				.andExpect(jsonPath("$.accountHolderName").value("Test User"))
				.andExpect(jsonPath("$.userName").value("testuser")).andExpect(jsonPath("$.balance").value("20"))
				.andExpect(jsonPath("$.tombstoned").value(true));
	}

	@Test
	public void testTransferMoneyFromAnAccountToAnother() throws Exception {

		this.mockMvc
				.perform(get("/createAccount").param("accountHolderName", "Test User 1").param("userName", "testuser1")
						.param("password", "testpassword1").param("initialBalance", "20"))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.accountId").value("4W9Z05L4VM4K"))
				.andExpect(jsonPath("$.accountHolderName").value("Test User 1"))
				.andExpect(jsonPath("$.userName").value("testuser1")).andExpect(jsonPath("$.balance").value("20"));

		this.mockMvc
				.perform(get("/createAccount").param("accountHolderName", "Test User 2").param("userName", "testuser2")
						.param("password", "testpassword2").param("initialBalance", "20"))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.accountId").value("H6VM3LXTCEAT"))
				.andExpect(jsonPath("$.accountHolderName").value("Test User 2"))
				.andExpect(jsonPath("$.userName").value("testuser2")).andExpect(jsonPath("$.balance").value("20"));

		this.mockMvc
				.perform(get("/transferMoneyFromAnAccountToAnother").param("fromAccountId", "4W9Z05L4VM4K")
						.param("toAccountId", "H6VM3LXTCEAT").param("transferAmount", "10"))
				.andDo(print()).andExpect(status().isOk());

		this.mockMvc.perform(get("/findAccountByUserName").param("userName", "testuser1")).andDo(print())
				.andExpect(status().isOk()).andExpect(jsonPath("$.accountId").value("4W9Z05L4VM4K"))
				.andExpect(jsonPath("$.accountHolderName").value("Test User 1"))
				.andExpect(jsonPath("$.userName").value("testuser1")).andExpect(jsonPath("$.balance").value("10"))
				.andExpect(jsonPath("$.tombstoned").value(false));

		this.mockMvc.perform(get("/findAccountByUserName").param("userName", "testuser2")).andDo(print())
				.andExpect(status().isOk()).andExpect(jsonPath("$.accountId").value("H6VM3LXTCEAT"))
				.andExpect(jsonPath("$.accountHolderName").value("Test User 2"))
				.andExpect(jsonPath("$.userName").value("testuser2")).andExpect(jsonPath("$.balance").value("30"))
				.andExpect(jsonPath("$.tombstoned").value(false));

		this.mockMvc
				.perform(get("/transferMoneyFromAnAccountToAnother").param("fromAccountId", "4W9Z05L4VM4K")
						.param("toAccountId", "H6VM3LXTCEAT").param("transferAmount", "10"))
				.andDo(print()).andExpect(status().isOk());

		this.mockMvc.perform(get("/findAccountByUserName").param("userName", "testuser1")).andDo(print())
				.andExpect(status().isOk()).andExpect(jsonPath("$.accountId").value("4W9Z05L4VM4K"))
				.andExpect(jsonPath("$.accountHolderName").value("Test User 1"))
				.andExpect(jsonPath("$.userName").value("testuser1")).andExpect(jsonPath("$.balance").value("0"))
				.andExpect(jsonPath("$.tombstoned").value(false));

		this.mockMvc.perform(get("/findAccountByUserName").param("userName", "testuser2")).andDo(print())
				.andExpect(status().isOk()).andExpect(jsonPath("$.accountId").value("H6VM3LXTCEAT"))
				.andExpect(jsonPath("$.accountHolderName").value("Test User 2"))
				.andExpect(jsonPath("$.userName").value("testuser2")).andExpect(jsonPath("$.balance").value("40"))
				.andExpect(jsonPath("$.tombstoned").value(false));

		this.mockMvc.perform(get("/findAllAccountActivityForAccountId").param("accountId", "4W9Z05L4VM4K"))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$[0].accountId").value("4W9Z05L4VM4K"))
				.andExpect(jsonPath("$[0].amount").value("10")).andExpect(jsonPath("$[0].credit").value("false"))
				.andExpect(jsonPath("$[0].accountId").value("4W9Z05L4VM4K"))
				.andExpect(jsonPath("$[0].amount").value("10")).andExpect(jsonPath("$[0].credit").value("false"));

		this.mockMvc.perform(get("/findAllAccountActivityForAccountId").param("accountId", "H6VM3LXTCEAT"))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$[0].accountId").value("H6VM3LXTCEAT"))
				.andExpect(jsonPath("$[0].amount").value("10")).andExpect(jsonPath("$[0].credit").value("true"))
				.andExpect(jsonPath("$[0].accountId").value("H6VM3LXTCEAT"))
				.andExpect(jsonPath("$[0].amount").value("10")).andExpect(jsonPath("$[0].credit").value("true"));
	}

	@Test
	public void testTransferMoneyFromAnAccountToAnotherFailureDueToToumbstoning() throws Exception {

		this.mockMvc
				.perform(get("/createAccount").param("accountHolderName", "Test User 1").param("userName", "testuser1")
						.param("password", "testpassword1").param("initialBalance", "20"))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.accountId").value("4W9Z05L4VM4K"))
				.andExpect(jsonPath("$.accountHolderName").value("Test User 1"))
				.andExpect(jsonPath("$.userName").value("testuser1")).andExpect(jsonPath("$.balance").value("20"));

		this.mockMvc
				.perform(get("/createAccount").param("accountHolderName", "Test User 2").param("userName", "testuser2")
						.param("password", "testpassword2").param("initialBalance", "20"))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.accountId").value("H6VM3LXTCEAT"))
				.andExpect(jsonPath("$.accountHolderName").value("Test User 2"))
				.andExpect(jsonPath("$.userName").value("testuser2")).andExpect(jsonPath("$.balance").value("20"));

		this.mockMvc
				.perform(get("/transferMoneyFromAnAccountToAnother").param("fromAccountId", "4W9Z05L4VM4K")
						.param("toAccountId", "H6VM3LXTCEAT").param("transferAmount", "10"))
				.andDo(print()).andExpect(status().isOk());

		this.mockMvc.perform(get("/findAccountByUserName").param("userName", "testuser1")).andDo(print())
				.andExpect(status().isOk()).andExpect(jsonPath("$.accountId").value("4W9Z05L4VM4K"))
				.andExpect(jsonPath("$.accountHolderName").value("Test User 1"))
				.andExpect(jsonPath("$.userName").value("testuser1")).andExpect(jsonPath("$.balance").value("10"))
				.andExpect(jsonPath("$.tombstoned").value(false));

		this.mockMvc.perform(get("/findAccountByUserName").param("userName", "testuser2")).andDo(print())
				.andExpect(status().isOk()).andExpect(jsonPath("$.accountId").value("H6VM3LXTCEAT"))
				.andExpect(jsonPath("$.accountHolderName").value("Test User 2"))
				.andExpect(jsonPath("$.userName").value("testuser2")).andExpect(jsonPath("$.balance").value("30"))
				.andExpect(jsonPath("$.tombstoned").value(false));

		this.mockMvc.perform(get("/deleteAccountByAccountID").param("accountId", "4W9Z05L4VM4K"));

		this.mockMvc
				.perform(get("/transferMoneyFromAnAccountToAnother").param("fromAccountId", "4W9Z05L4VM4K")
						.param("toAccountId", "H6VM3LXTCEAT").param("transferAmount", "10"))
				.andDo(print()).andExpect(status().is4xxClientError());
	}
}