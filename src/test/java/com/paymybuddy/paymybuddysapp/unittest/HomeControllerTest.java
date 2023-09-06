package com.paymybuddy.paymybuddysapp.unittest;

import com.paymybuddy.paymybuddysapp.controller.HomeController;
import com.paymybuddy.paymybuddysapp.model.PayMyBuddyBankAccount;
import com.paymybuddy.paymybuddysapp.model.User;
import com.paymybuddy.paymybuddysapp.service.BankAccountService;
import com.paymybuddy.paymybuddysapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = HomeController.class)
@AutoConfigureMockMvc(addFilters = false)

public class HomeControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    UserService userService;
    @MockBean
    BankAccountService bankAccountService;

    @WithMockUser("currentUser@test")
    @Test
    public void home_returnHomeView() throws Exception {
        User currentUser = getCurrentUser();
        Mockito.when(userService.getUserByEmail(currentUser.getEmail())).thenReturn(currentUser);
        mockMvc.perform(MockMvcRequestBuilders.get("/home"))
                .andExpect(MockMvcResultMatchers.view().name("home"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("transfer"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("buddies"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("personalBankAccountToFile"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("payMyBuddyBankAccount"));
    }

    private User getCurrentUser() {
        User currentUser = new User();
        currentUser.setId(1);
        currentUser.setEmail("currentUser@test");
        currentUser.setFirstName("test");
        currentUser.setLastName("tset");
        currentUser.setPassword("1234");

        PayMyBuddyBankAccount currentUserPayMyBuddyBankAccount = new PayMyBuddyBankAccount();
        currentUserPayMyBuddyBankAccount.setAccountBalance(500.00);
        currentUser.setPayMyBuddyBankAccount(currentUserPayMyBuddyBankAccount);

        return currentUser;
    }
}