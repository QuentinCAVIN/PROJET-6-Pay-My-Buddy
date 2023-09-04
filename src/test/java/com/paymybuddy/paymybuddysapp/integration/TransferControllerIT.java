package com.paymybuddy.paymybuddysapp.integration;


import com.paymybuddy.paymybuddysapp.dto.TransferDto;
import com.paymybuddy.paymybuddysapp.model.PayMyBuddyBankAccount;
import com.paymybuddy.paymybuddysapp.model.PersonalBankAccount;
import com.paymybuddy.paymybuddysapp.model.Transfer;
import com.paymybuddy.paymybuddysapp.model.User;
import com.paymybuddy.paymybuddysapp.repository.BankAccountRepository;
import com.paymybuddy.paymybuddysapp.repository.TransferRepository;
import com.paymybuddy.paymybuddysapp.repository.UserRepository;

import com.paymybuddy.paymybuddysapp.service.BankAccountService;
import com.paymybuddy.paymybuddysapp.service.TransferService;
import com.paymybuddy.paymybuddysapp.service.UserService;
import jakarta.transaction.Transactional;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.List;
import java.util.Optional;

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

    @Autowired
    BankAccountRepository bankAccountRepository;

    @Autowired
    TransferRepository transferRepository;
    private static User currentUser;
    private static User buddyToAdd;

    private static TransferDto transferDto;

    private PersonalBankAccount masterBankAccount;


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
        buddyToAdd.setEmail("buddy@test");
        buddyToAdd.setFirstName("tset");
        buddyToAdd.setLastName("test");
        buddyToAdd.setPassword("4321");
       /* buddyToAdd.setUsersConnexions(Arrays.asList(currentUser));
        buddyToAdd.setUsersConnected(Arrays.asList(currentUser));*/
        PayMyBuddyBankAccount buddyToAddUserPayMyBuddyBankAccount = new PayMyBuddyBankAccount();
        buddyToAddUserPayMyBuddyBankAccount.setAccountBalance(500.00);
        buddyToAdd.setPayMyBuddyBankAccount(buddyToAddUserPayMyBuddyBankAccount);

        transferDto = new TransferDto();
        transferDto.setAmount(50.00);
        transferDto.setBuddyUsername ("buddy@test");
        transferDto.setDescription("Test");
    }


    @BeforeEach
    public void setup() throws Exception {
        userRepository.deleteAll();
        bankAccountRepository.deleteAll();

        masterBankAccount = new PersonalBankAccount();
        masterBankAccount.setAccountBalance(10000);
        masterBankAccount.setIban("666");

        bankAccountRepository.save(masterBankAccount);// TODO REMARQUE C'est la preuve que je peux passer par le
        // BankAccountRepository pour gérer les deux classes qui héritent de BankAccount. Modifier En fin de projet


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
                .andExpect(MockMvcResultMatchers.model().attributeExists("transfer"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("transfers"));
    }



    @Test
    @WithMockUser("currentUser@test")
    @DisplayName("Validate the addBuddy form should create a new connexion")
    public void validateAddBuddyFormShouldCreateNewConnexionInDB() throws Exception {

        //User Add Buddy
        mockMvc.perform(MockMvcRequestBuilders.post("/transfer/addBuddy")
                        .param("email", buddyToAdd.getEmail()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/transfer"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("Your new buddy is added!")))
                .andExpect(MockMvcResultMatchers.model().attributeExists("buddies"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("buddy"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("transfer"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("transfers"));

        //checks that user and his buddy are connected
        User currentUser = userService.getUserByEmail(this.currentUser.getEmail());
        User userAdded = userService.getUserByEmail(buddyToAdd.getEmail());
        assertThat(currentUser.getUsersConnexions().get(0).getEmail()).isEqualTo(buddyToAdd.getEmail());
        assertThat(userAdded.getUsersConnected().get(0).getEmail()).isEqualTo(this.currentUser.getEmail());
    }


    @Test
    @WithMockUser("currentUser@test")
    @DisplayName("Validating the addBuddy form without filling in the fields should display error messages")
    public void validateBuddysFormWithoutFillingInFieldShouldDisplayErrorMessages() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/transfer/addBuddy"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/transfer"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("Please fill in your buddy")))
                .andExpect(MockMvcResultMatchers.model().attributeExists("buddies"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("buddy"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("transfer"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("transfers"));
    }


    @Test
    @WithMockUser("currentUser@test")
    @DisplayName("Validating the addBuddy's form with a wrong email should display error messages")
    public void validateBuddysFormWithWrongEmailShouldDisplayErrorMessages() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/transfer/addBuddy")
                        .param("email", "wrong@email"))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/transfer"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("There is no account associated to wrong@email")))
                .andExpect(MockMvcResultMatchers.model().attributeExists("buddies"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("buddy"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("transfer"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("transfers"));
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
                .andExpect(MockMvcResultMatchers.model().attributeExists("transfer"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("transfers"));
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
                .andExpect(MockMvcResultMatchers.model().attributeExists("transfer"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("transfers"));
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

        TransferDto transferDto = new TransferDto();
        transferDto.setAmount(50.00);
        transferDto.setBuddyUsername ("buddy@test");
        transferDto.setDescription("Test");

        double accountBalanceOfMasterBankAccountBeforeTransfer = masterBankAccount.getAccountBalance();

        //Add Buddy for transfer
        validateAddBuddyFormShouldCreateNewConnexionInDB();

        //currentUser send 50€ to his buddy
        mockMvc.perform(MockMvcRequestBuilders.post("/transfer/sendMoney")
                        .param("buddyUsername", transferDto.getBuddyUsername())
                        .param("amount", String.valueOf(transferDto.getAmount()))
                        .param("description", transferDto.getDescription()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/transfer"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("Your transfer is sent to " +
                                transferDto.getBuddyUsername() + "!")))
                .andExpect(MockMvcResultMatchers.model().attributeExists("buddies"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("buddy"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("transfer"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("transfers"));

        //checks that the bank accounts of currentUser and his buddy correspond to the transfer made
        PayMyBuddyBankAccount currentUserPayMyBuddyBankAccount = (userService.getUserByEmail(currentUser.getEmail())
                .getPayMyBuddyBankAccount());
        PayMyBuddyBankAccount receivingUserPayMyBuddyBankAccount = (userService.getUserByEmail(buddyToAdd.getEmail())
                .getPayMyBuddyBankAccount());
        assertThat(currentUserPayMyBuddyBankAccount.getAccountBalance())
                .isEqualTo(500.00 - (50.00 + (50*0.05)));//5% levy
        assertThat(receivingUserPayMyBuddyBankAccount.getAccountBalance()).isEqualTo(500.00 + 50.00);

        //Checks that the the 5% is well credited to the masterBankAccount
        assertThat( masterBankAccount.getAccountBalance())
                .isEqualTo(accountBalanceOfMasterBankAccountBeforeTransfer + (50*0.05));


        //Checks that the transfer is present in DB
        Optional <Transfer> OptionalTransfer = transferRepository.findById(1);
        Transfer transfer = OptionalTransfer.get();
        assertThat(transfer.getAmount()).isEqualTo(transferDto.getAmount());
        //TODO: Dans le processus d'enregistrement d'un transfer je me suis passé des méthodes utilitaires
        // pour associer transfer et compte banquaire, car Jpa le fait a priori tout seul.
        // Malheureusement ça ne fonctionne pas dans les test d'intégration, je ne peut pas récupérer un virememt
        // a partir d'un compte banquaire. Pour vérifier qu'un objet transfer à bien été créé, je peux soit utiliser
        // a nouveau ces méthodes utilitaires, soit créer une méthode pour récupérer un transfer dans le repository
        // (qui ne servira que pour les tests), soit ne pas tester le faite que la méthode crée un virement
        // en base de donnée
        // EDIT j'ai utilisé findById pour ne pas créer une nouvelle methode, ça me semble fragile (si l'id auto généré n'est pas 1)

    }
    @Test
    @DisplayName("SendMoney form validation without buddy selection should display an error message")
    @WithMockUser("currentUser@test")
    public void sendMoneyValidationWithoutBuddyShouldDisplayErrorMessage() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/transfer/sendMoney")
                .param("amount", "50")
                .param("description", "message"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/transfer"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("Please select a buddy")))
                .andExpect(MockMvcResultMatchers.model().attributeExists("buddies"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("buddy"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("transfer"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("transfers"));
    }

    @Test
    @DisplayName("SendMoney form validation with wrong amount selection should display an error message")
    @WithMockUser("currentUser@test")
    public void sendMoneyWithWrongAmountShouldDisplayErrorMessage() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/transfer/sendMoney")
                        .param("buddyUsername", transferDto.getBuddyUsername())
                        .param("amount", "-10")
                        .param("description", "message"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/transfer"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("Incorrect amount value")))
                .andExpect(MockMvcResultMatchers.model().attributeExists("buddies"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("buddy"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("transfer"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("transfers"));
    }

    @Test
    @DisplayName("SendMoney form validation without enough money selection should display an error message")
    @WithMockUser("currentUser@test")
    public void sendMoneyWithoutEnoughMoneyShouldDisplayErrorMessage() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/transfer/sendMoney")
                        .param("buddyUsername", transferDto.getBuddyUsername())
                        .param("amount", "100000")
                        .param("description", "message"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/transfer"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString("you do not have enough money in your account")))
                .andExpect(MockMvcResultMatchers.model().attributeExists("buddies"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("buddy"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("transfer"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("transfers"));
    }
}