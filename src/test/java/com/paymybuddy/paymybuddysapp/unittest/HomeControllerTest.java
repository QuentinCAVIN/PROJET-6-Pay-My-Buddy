package com.paymybuddy.paymybuddysapp.unittest;

import com.paymybuddy.paymybuddysapp.controller.AuthentificationController;
import com.paymybuddy.paymybuddysapp.controller.HomeController;
import com.paymybuddy.paymybuddysapp.dto.UserDto;
import com.paymybuddy.paymybuddysapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = HomeController.class)
@AutoConfigureMockMvc(addFilters = false)

public class HomeControllerTest {
  /*  @Autowired
    MockMvc mockMvc;
    @MockBean
    UserService userService;
    @Test
    public void home_returnHomeView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/home"))
                .andExpect(MockMvcResultMatchers.view().name("home"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("users"));
    }*/
}

