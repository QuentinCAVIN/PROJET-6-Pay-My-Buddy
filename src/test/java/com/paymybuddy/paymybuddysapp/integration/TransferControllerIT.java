package com.paymybuddy.paymybuddysapp.integration;


import com.paymybuddy.paymybuddysapp.dto.InternalTransferDto;
import com.paymybuddy.paymybuddysapp.mapper.TransferMapper;
import com.paymybuddy.paymybuddysapp.model.PayMyBuddyBankAccount;
import com.paymybuddy.paymybuddysapp.model.User;
import com.paymybuddy.paymybuddysapp.repository.UserRepository;

import com.paymybuddy.paymybuddysapp.service.UserService;
import jakarta.transaction.Transactional;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
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


    private static User currentUser;
    private static User buddyToAdd;
    /*private static InternalTransferDto transferDto;*/ //TODO: a effacer aprés le test pour créer un transfer

    @BeforeAll
    public static void setupBeforAll() throws Exception {
        currentUser = new User();
        buddyToAdd = new User();

        currentUser.setId(1);
        currentUser.setEmail("currentUser@test");
        currentUser.setFirstName("test");
        currentUser.setLastName("tset");
        currentUser.setPassword("1234");
        /*currentUser.setUsersConnexions(Arrays.asList(buddyToAdd));
        currentUser.setUsersConnected(Arrays.asList(buddyToAdd));*/
        PayMyBuddyBankAccount currentUserPayMyBuddyBankAccount= new PayMyBuddyBankAccount();
        currentUserPayMyBuddyBankAccount.setAccountBalance(500.00);
        currentUser.setPayMyBuddyBankAccount(currentUserPayMyBuddyBankAccount);

        buddyToAdd.setId(2);
        buddyToAdd.setEmail("receivingUser@test");
        buddyToAdd.setFirstName("tset");
        buddyToAdd.setLastName("test");
        buddyToAdd.setPassword("4321");
       /* buddyToAdd.setUsersConnexions(Arrays.asList(currentUser));
        buddyToAdd.setUsersConnected(Arrays.asList(currentUser));*/
        PayMyBuddyBankAccount buddyToAddUserPayMyBuddyBankAccount = new PayMyBuddyBankAccount();
        buddyToAddUserPayMyBuddyBankAccount.setAccountBalance(500.00);
        buddyToAdd.setPayMyBuddyBankAccount(buddyToAddUserPayMyBuddyBankAccount);


    }


    @BeforeEach
    public void setup() throws Exception {

        userRepository.deleteAll();
        userRepository.save(currentUser);
        userRepository.save(buddyToAdd);
    }


    @Test
    @WithMockUser("currentUser@test")
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
    @WithMockUser("currentUser@test")
    @DisplayName("Validate the addBuddy form should create a new connexion")
    public void validateAddBuddyFormShouldCreateNewConnexionInDB() throws Exception {

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
    @WithMockUser("currentUser@test")
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
    @WithMockUser("currentUser@test")
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
    @WithMockUser("currentUser@test")
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
    @WithMockUser("currentUser@test")
    @DisplayName("Validate the buddy's form with an email already registered should display an error message")
    public void validateBuddysFormWithEmailAlreadyRegisteredShouldDisplayErrorMessage() throws Exception {

        //First buddy's registration
        validateAddBuddyFormShouldCreateNewConnexionInDB();

        //Second registration with the same email
        mockMvc.perform(MockMvcRequestBuilders.post("/transfer/addBuddy")
                        .param("email", buddyToAdd.getEmail()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/transfer"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString(buddyToAdd.getFirstName() + " "
                                + buddyToAdd.getLastName() + " is already add to your buddies!")))

                // information retrieved in the model attribute
                .andExpect(MockMvcResultMatchers.model().attributeExists("buddy"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("buddies"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("transfer"));
    }

    @Test
    @WithMockUser("currentUser@test")
    @DisplayName("/deleteBuddy should delete current user Buddy's")
    public void deleteConnectionShouldDeleteUserBuddys() throws Exception {

        //mockMvc

        //Add Buddy
        validateAddBuddyFormShouldCreateNewConnexionInDB();

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

    @Test
    @DisplayName("/sendMoney should transfer money to selected user")
    @WithMockUser("currentUser@test")
    public void sendMoneyShouldTransferMoneyToSelectedUser() throws Exception {

        InternalTransferDto transferDto = new InternalTransferDto();
        transferDto.setAmount(50.00);
        /*transferDto.setUsernameOfSenderAccount("currentUser@test");*/ // TODO: finir le test et effacer : inutile
        transferDto.setUsernameOfRecipientAccount("receivingUser@test");
        transferDto.setDescription("Test");
        transferDto.setDate("23/01/2024"); // TODO: finir le test et effacer : inutile

        //Add Buddy for transfer
        validateAddBuddyFormShouldCreateNewConnexionInDB();

        //currentUser send 50€ to his buddy
        mockMvc.perform(MockMvcRequestBuilders.post("/transfer/sendMoney")
                        .param("usernameOfRecipientAccount", transferDto.getUsernameOfRecipientAccount())
                        .param("amount", String.valueOf(transferDto.getAmount())))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/transfer?success"));

        //checks that the bank accounts of currentUser and his buddy correspond to the transfer made
        PayMyBuddyBankAccount currentUserPayMyBuddyBankAccount = (userService.getUserByEmail(currentUser.getEmail())
                .getPayMyBuddyBankAccount());
        PayMyBuddyBankAccount receivingUserPayMyBuddyBankAccount = (userService.getUserByEmail(buddyToAdd.getEmail())
                .getPayMyBuddyBankAccount());

        assertThat(currentUserPayMyBuddyBankAccount.getAccountBalance()).isEqualTo(500.00 - 50.00);
        assertThat(receivingUserPayMyBuddyBankAccount.getAccountBalance()).isEqualTo(500.00 + 50.00);
        //TODO: ARRET DU TEST, Il Faudra vérifier que le transfer est bien stocké en base de donée + la vue success est bien gérée
    }

/* CI DESSOUS METHODE A TESTER

  @PostMapping("/transfer/sendMoney")
    public String sendMoney(@ModelAttribute("transfer") InternalTransferDto internalTransferDto,
                            @AuthenticationPrincipal UserDetails userDetails,
                            Model model, BindingResult result ) {

        User currentUser = userService.getUserByEmail(userDetails.getUsername());
        User userSelected = userService.getUserByEmail(internalTransferDto.getUsernameOfRecipientAccount());

        BankAccount senderAccount = currentUser.getPayMyBuddyBankAccount();
        BankAccount recipientAccount = userSelected.getPayMyBuddyBankAccount();
        double transferAmount = internalTransferDto.getAmount();

        if (transferAmount <= 0){
            result.rejectValue("amount", null,
                    "Incorrect amount value");
        }

        else if (senderAccount.getAccountBalance() <= transferAmount){
            result.rejectValue("amount", null,
                    "you do not have enough money in your account");

        }

        if (result.hasErrors()) {

            //TODO : vérifier si il n'y a pas un meilleur moyen que de recopier la methode /transfer
            // Pour recharger la meme page avec un message d'erreur approprié

            UserDto buddy = new UserDto();
            model.addAttribute("buddy", buddy);

            List<User> buddies = currentUser.getUsersConnexions();
            model.addAttribute("buddies", buddies);

            InternalTransferDto transfer = new InternalTransferDto();
            model.addAttribute("transfer", transfer);//

            return "/transfer";
        }

            Transfer transfer = transferMapper.convertInternalTransferDtoToTransfer(internalTransferDto);

            bankAccountService.transfer(transfer);
            return "redirect:/transfer?success";
            // TODO : rajouter un message de confirmation attention a ne pas générer d'autre message de succès inappropriées
    }*/


}