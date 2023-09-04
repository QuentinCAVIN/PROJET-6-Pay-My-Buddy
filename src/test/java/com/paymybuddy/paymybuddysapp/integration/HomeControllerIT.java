package com.paymybuddy.paymybuddysapp.integration;

import jakarta.transaction.Transactional;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerIT {

    /*
    @Test
    @WithMockUser("currentUser@test")
    @DisplayName("An authenticated user should access his home page")
    public void authenticatedUserShouldAccessHisHomePage() throws Exception {
        userRepository.save(currentUser);
        mockMvc.perform(MockMvcRequestBuilders.get("/home"))

                .andExpect(MockMvcResultMatchers.view().name("home"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("users"));
    }*/
}
