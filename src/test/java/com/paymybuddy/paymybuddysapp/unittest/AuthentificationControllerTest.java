package com.paymybuddy.paymybuddysapp.unittest;

import com.paymybuddy.paymybuddysapp.config.SpringSecurityConfig;
import com.paymybuddy.paymybuddysapp.controller.AuthentificationController;
import com.paymybuddy.paymybuddysapp.dto.UserDto;
import com.paymybuddy.paymybuddysapp.model.User;
import com.paymybuddy.paymybuddysapp.service.UserService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AuthentificationController.class)
@AutoConfigureMockMvc(addFilters = false)

public class AuthentificationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;
    AuthentificationController authentificationController;

    private static UserDto dummy =new UserDto();

    @BeforeAll
    public static void setup() {
        dummy = new UserDto(1, "test", "tset", "test@tset",
                "1234", null,null);
    }

    @Test
    public void login_returnLoginView() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(MockMvcResultMatchers.view().name("login"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void showRegistrationForm_returnNewAccountView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/registration"))

                .andExpect(MockMvcResultMatchers.view().name("newAccount"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"));
    }

    @Test
    @WithMockUser
    public void saveUser_createNewUser_whenCallWithCorrectArguments() throws Exception {

        Mockito.when(userService.getUserByEmail(dummy.getEmail())).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/registration/saveUser")
                .param("firstName", dummy.getFirstName())
                .param("lastName", dummy.getLastName())
                .param("email", dummy.getEmail())
                .param("password", dummy.getPassword()))

                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login?success"));

        Mockito.verify(userService,Mockito.times(1)).createNewUser(any(UserDto.class));
    }

    @Test
    @WithMockUser
    public void saveUser_dontCreateUserAndDisplayErrorMessage_whenUserIsAlreadyRegistered() throws Exception {

        Mockito.when(userService.getUserByEmail(dummy.getEmail())).thenReturn(new User());

        mockMvc.perform(MockMvcRequestBuilders.post("/registration/saveUser")
                        .param("firstName", dummy.getFirstName())
                        .param("lastName", dummy.getLastName())
                        .param("email", dummy.getEmail())
                        .param("password", dummy.getPassword()))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/newAccount"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("There is already an account associated")))

                .andExpect(MockMvcResultMatchers.model().attributeExists("user"));

        Mockito.verify(userService,Mockito.times(0)).createNewUser(any(UserDto.class));
    }

    @Test
    @WithMockUser
    public void saveUser_dontCreateUserAndDisplayErrorMessage_whenFormIsValidateWithoutFillingInField() throws Exception {

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
                        .string(CoreMatchers.containsString("Password should not be empty")))
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"));

        Mockito.verify(userService,Mockito.times(0)).createNewUser(any(UserDto.class));
    }

    @Test
    public void home_returnHomeView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/home"))

                .andExpect(MockMvcResultMatchers.view().name("home"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("users"));
    }
}

