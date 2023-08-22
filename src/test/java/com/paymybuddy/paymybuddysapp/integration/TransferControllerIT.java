package com.paymybuddy.paymybuddysapp.integration;


import com.paymybuddy.paymybuddysapp.dto.UserDto;
import com.paymybuddy.paymybuddysapp.model.User;
import com.paymybuddy.paymybuddysapp.repository.UserRepository;

import com.paymybuddy.paymybuddysapp.service.UserService;
import jakarta.transaction.Transactional;
import org.hamcrest.CoreMatchers;
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


import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc

public class TransferControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService ;


    UserDto currentUser = new UserDto(1, "test", "tset", "test@tset", "1234", null,null);
    UserDto buddyToAdd = new UserDto(2, "tset", "test", "tset@test", "4321", null,null);




    @BeforeEach
    public void setup() throws Exception {

        userRepository.deleteAll();
        userService.createNewUser(currentUser);
        userService.createNewUser(buddyToAdd);
    }


    @Test
    @WithMockUser("test@tset")
    @DisplayName("Accessing /transfer should display the transfer view")
    public void authenticatedUserShouldAccessHisHomePage() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/transfer"))

                .andExpect(MockMvcResultMatchers.view().name("transfer"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("buddies"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("buddy"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("transfer"));
    }



    @Test
    @WithMockUser("test@tset")
    @DisplayName("Validate the addBuddy form should create a new connexion")
    public void validateAddBuddyFormShouldCreateNewUserInDB() throws Exception {

        //User Add Buddy
        mockMvc.perform(MockMvcRequestBuilders.post("/transfer/addBuddy")
                        .param("email", buddyToAdd.getEmail()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/transfer?success"));

        //checks that user and his buddy are connected
        User currentUser = userService.getUserByEmail(this.currentUser.getEmail());
        User userAdded = userService.getUserByEmail(buddyToAdd.getEmail());
        assertThat(currentUser.getUsersConnexions().get(0).getEmail()).isEqualTo(buddyToAdd.getEmail());
        assertThat(userAdded.getUsersConnected().get(0).getEmail()).isEqualTo(this.currentUser.getEmail());
    }


    @Test
    @WithMockUser("test@tset")
    @DisplayName("Validating the form without filling in the fields should display error messages")
    public void validateBuddysFormWithoutFillingInFieldShouldDisplayErrorMessages() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/transfer/addBuddy"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/transfer"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("Please fill in your buddy")))
                .andExpect(MockMvcResultMatchers.model().attributeExists("buddies"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("buddy"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("transfer"));
    }


    @Test
    @WithMockUser("test@tset")
    @DisplayName("Validating the buddy's form with a wrong email should display error messages")
    public void validateBuddysFormWithWrongEmailShouldDisplayErrorMessages() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/transfer/addBuddy")
                        .param("email", "wrong@email"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/transfer"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("There is no account associated to wrong@email")))
                .andExpect(MockMvcResultMatchers.model().attributeExists("buddies"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("buddy"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("transfer"));
    }


    @Test
    @WithMockUser("test@tset")
    @DisplayName("Validating the buddy's form with own email should display error messages")
    public void validateBuddysFormWithOwnEmailShouldDisplayErrorMessages() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/transfer/addBuddy")
                        .param("email", currentUser.getEmail()))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/transfer"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("go get some friends")))
                .andExpect(MockMvcResultMatchers.model().attributeExists("buddies"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("buddy"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("transfer"));
    }

    @Test
    @WithMockUser("test@tset")
    @DisplayName("Validate the buddy's form with an email already registered should display an error message")
    public void validateBuddysFormWithEmailAlreadyRegisteredShouldDisplayErrorMessage() throws Exception {

        //First buddy's registration
        validateAddBuddyFormShouldCreateNewUserInDB();

        //Second registration with the same email
        mockMvc.perform(MockMvcRequestBuilders.post("/transfer/addBuddy")
                        .param("email", buddyToAdd.getEmail()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/transfer"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString(buddyToAdd.getFirstName() + " "
                                + buddyToAdd.getLastName() + " is already add to your buddies!")))

                //TODO : Voir si il est necessaire de retirer le test de la présence du message d'erreur sur la page,
                // pour se limiter au test du backend

                // information retrieved in the model attribute
                .andExpect(MockMvcResultMatchers.model().attributeExists("buddy"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("buddies"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("transfer"));
    }

    @Test
    @WithMockUser("test@tset")
    @DisplayName("/deleteBuddy should delete current user Buddy's")
    public void deleteConnectionShouldDeleteUserBuddys() throws Exception {

        //Add Buddy
        validateBuddysFormWithoutFillingInFieldShouldDisplayErrorMessages();

        //Remove Buddy
        mockMvc.perform(MockMvcRequestBuilders.get("/transfer/deleteBuddy")
                .param("email", buddyToAdd.getEmail()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/transfer"));


        //check that current user and his buddy are not connected anymore
        User currentUser = userService.getUserByEmail(this.currentUser.getEmail());
        User userAdded = userService.getUserByEmail(buddyToAdd.getEmail());
        assertThat(currentUser.getUsersConnexions().isEmpty());
        assertThat(userAdded.getUsersConnected().isEmpty());
    }


}