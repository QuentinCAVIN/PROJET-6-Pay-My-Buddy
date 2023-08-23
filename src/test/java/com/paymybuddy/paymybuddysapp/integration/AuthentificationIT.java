package com.paymybuddy.paymybuddysapp.integration;


import com.paymybuddy.paymybuddysapp.dto.UserDto;
import com.paymybuddy.paymybuddysapp.repository.UserRepository;

import com.paymybuddy.paymybuddysapp.service.UserService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    UserRepository userRepository;

    UserDto dummy = new UserDto(1, "test", "tset", "test@tset", "1234", null,null);

    @Autowired
    UserService userService;

    @BeforeEach
    public void setup() {

        userRepository.deleteAll();

    }


    @Test
    @WithMockUser
    @DisplayName("An authenticated user should access his home page")
    public void authenticatedUserShouldAccessHisHomePage() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/home"))

                .andExpect(MockMvcResultMatchers.view().name("home"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("users"));
    }


    @Test
    @WithAnonymousUser
    @DisplayName("An unauthenticated user should be redirected to the login page")
    public void unauthenticatedUserShouldBeRedirectedToLoginPage() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/home"))
                .andDo(MockMvcResultHandlers.print())//Dispensable
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/login"));
    }


    @Test
    @WithAnonymousUser
    @DisplayName("Accessing /login should display the login view ")
    public void loginShouldDisplayLoginView() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("login"));
    }


    @Test
    @WithAnonymousUser
    @DisplayName("An unregistered user should not log in")
    public void unregistredUserShouldNotLogIn() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .param("username", dummy.getEmail())
                        .param("password", dummy.getPassword()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login?error"));
    }


    @Test
    @WithAnonymousUser
    @DisplayName("A registered user can log in")
    public void registeredUserCanLogIn() throws Exception {

        //User registers
        validateRegistrationFormShouldCreateNewUserInDB();

        //User log in
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .param("username", dummy.getEmail())
                        .param("password", dummy.getPassword()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/home"));
    }


    @Test
    @WithAnonymousUser
    @DisplayName("Accessing /registration should display the registration view with a user model attribute")
    public void registrationShouldDisplayRegistrationViewWithModelAttribute() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/registration"))

                .andExpect(MockMvcResultMatchers.view().name("newAccount"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"));
    }


    @Test
    @WithAnonymousUser
    @DisplayName("Validate the registration form should create a new user in database")
    public void validateRegistrationFormShouldCreateNewUserInDB() throws Exception {

        //User registers
        mockMvc.perform(MockMvcRequestBuilders.post("/registration/saveUser")
                        .param("firstName", dummy.getFirstName())
                        .param("lastName", dummy.getLastName())
                        .param("email", dummy.getEmail())
                        .param("password", dummy.getPassword()))

                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login?success"));

        //Check if user is present in DB
        UserDto userInDB = userService.getUserDtoByEmail(dummy.getEmail());
        assertThat(userInDB.getEmail()).isEqualTo(dummy.getEmail());
    }


    @Test
    @WithAnonymousUser
    @DisplayName("Validate the form with an email already registered should display an error message")
    public void validateFormWithEmailAlreadyRegisteredShouldDisplayErrorMessage() throws Exception {

        //First registration
        validateRegistrationFormShouldCreateNewUserInDB();

        //Second registration with the same email
        mockMvc.perform(MockMvcRequestBuilders.post("/registration/saveUser")
                        .param("firstName", dummy.getFirstName())
                        .param("lastName", dummy.getLastName())
                        .param("email", dummy.getEmail())
                        .param("password", dummy.getPassword()))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/newAccount"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("There is already an account associated with the "
                                + dummy.getEmail() + " email.")))

                .andExpect(MockMvcResultMatchers.model().attributeExists("user"));

                /*.andExpect(MockMvcResultMatchers.model().attribute("user", dummy.getEmail()));*/
                //Nécessaire de redéfinir la methode equals pour utiliser cette ligne
                //Permet d'être plus précis dans la vérification
    }
    @Test
    @WithAnonymousUser
    @DisplayName("Validating the form without filling in the fields should display error messages")
    public void validateFormWithoutFillingInFieldShouldDisplayErrorMessages() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/registration/saveUser"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/newAccount"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("First name should not be empty")))
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("Last name should not be empty")))
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("Email should not be empty")))
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("Password should not be empty")));
    }


    @Test
    @WithAnonymousUser
    @DisplayName("A user can log out")
    public void userCanLogOut() throws Exception {

        //User registers and log in
        registeredUserCanLogIn();

        //User log out
        mockMvc.perform(MockMvcRequestBuilders.get("/logout"));

        //User cant connect to his home page
        unauthenticatedUserShouldBeRedirectedToLoginPage();
    }

}