package com.paymybuddy.paymybuddysapp.integration;


import com.paymybuddy.paymybuddysapp.service.UserService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import static org.assertj.core.api.Assertions.assertThat;



@SpringBootTest
@AutoConfigureMockMvc

public class AuthentificationIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;


    @Nested
    @DisplayName("When everything goes as planned")
    class WhenValidationIsSuccessful {

        @Test
        @WithAnonymousUser
        @DisplayName("Accessing /login should return OK status code and display the login view ")
        public void loginReturnStotusOKAndLoginView() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.view().name("login"));

            /*.andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString("a")))*/;
            // Pour vérifier un contenu précis dans la page

        }

        @Test
        @WithAnonymousUser
        @DisplayName("An unauthenticated user should be redirected to the login page")
        public void unauthenticatedUserShouldBeRedirectedToLoginPage() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders.get("/home"))
                    .andDo(MockMvcResultHandlers.print())//Dispensable
                    .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                    .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"))
                    //TODO: Voir si je remplace par un chemin relatif
                    /*.andExpect(MockMvcResultMatchers.view().name("login"))*/;
            /*.andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString("a")))*/;

        }


        @Test
        @WithAnonymousUser
        @DisplayName("Accessing /registration should return OK status code " +
                "and display the registration view with a user model attribute")
        public void registrationReturnStotusOKAndRegistrationViewWithModelAttribute() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders.get("/registration"))

                    .andExpect(MockMvcResultMatchers.view().name("newAccount"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.model().attributeExists("user"));

            /*.andExpect(MockMvcResultMatchers.model().attribute("user",user)); Pour vérifier une valeur précise*/
        }

        @Test
        @WithAnonymousUser
        @DisplayName("Validate the registration form creates a new user")
        public void validateRegistrationFormCreateNewUser() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders.post("/registration/saveUser")
                    .param("firstName","test")
                    .param("lastName", "tset")
                    .param("email", "test@tset")
                    .param("password", "1234"))

                    .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                    .andExpect(MockMvcResultMatchers.redirectedUrl("/registration?success"));

           assertThat(userService.getUserByEmail("test@tset").getFirstName()).isEqualTo("test");
        }

        @Test
        @WithMockUser
        @DisplayName("an authenticated user should access his home page")
        public void authenticatedUserShouldAccessHisHomePage() throws Exception {

            mockMvc.perform(MockMvcRequestBuilders.get("/home"))

                    .andExpect(MockMvcResultMatchers.view().name("home"))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.model().attributeExists("users"));
        }
    }

    @Nested
    @DisplayName("When everything goes as planned")
    class WhenValidationFailed {

    }
}