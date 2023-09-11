package com.paymybuddy.paymybuddysapp.integration;

import com.paymybuddy.paymybuddysapp.model.PayMyBuddyBankAccount;
import com.paymybuddy.paymybuddysapp.model.User;
import com.paymybuddy.paymybuddysapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc

public class HomeControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    private static User currentUser;

    @BeforeAll
    public static void setupBeforeAll() {
        currentUser = new User();
        currentUser.setId(1);
        currentUser.setEmail("currentUser@test");
        currentUser.setFirstName("test");
        currentUser.setLastName("tset");
        currentUser.setPassword("1234");
        currentUser.setPayMyBuddyBankAccount(new PayMyBuddyBankAccount());
    }

    @BeforeEach
    public void setupBeforeEach() {
        userRepository.deleteAll();
        userRepository.save(currentUser);
    }

    @Test
    @WithMockUser("currentUser@test")
    @DisplayName("/home should display home page")
    @Transactional
    public void HomeShouldDisplayHomePage() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/home"))

                .andExpect(MockMvcResultMatchers.view().name("home"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("buddies"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("personalBankAccountToFile"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("personalBankAccountToDisplay"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("payMyBuddyBankAccount"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("transfer"));
    }

    @WithMockUser("currentUser@test")
    @Test
    public void linkPersonalBankAccountTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/home/linkPersonalBankAccount")
                        .param("iban", "666")
                        .param("accountBalance", "1000"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

    @WithMockUser("currentUser@test")
    @Test
    public void creditAndCashInTest() throws Exception {
        linkPersonalBankAccountTest();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/home/creditAndCashIn")
                        .param("button", "credit"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
