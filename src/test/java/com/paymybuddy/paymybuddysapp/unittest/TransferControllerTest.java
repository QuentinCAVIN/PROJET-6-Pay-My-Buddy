package com.paymybuddy.paymybuddysapp.unittest;

import com.paymybuddy.paymybuddysapp.controller.AuthentificationController;
import com.paymybuddy.paymybuddysapp.controller.TransferController;
import com.paymybuddy.paymybuddysapp.dto.TransferDto;
import com.paymybuddy.paymybuddysapp.mapper.TransferMapper;
import com.paymybuddy.paymybuddysapp.model.PayMyBuddyBankAccount;
import com.paymybuddy.paymybuddysapp.model.PersonalBankAccount;
import com.paymybuddy.paymybuddysapp.model.Transfer;
import com.paymybuddy.paymybuddysapp.model.User;
import com.paymybuddy.paymybuddysapp.service.BankAccountService;
import com.paymybuddy.paymybuddysapp.service.TransferService;
import com.paymybuddy.paymybuddysapp.service.UserService;
import net.bytebuddy.matcher.ElementMatcher;
import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = TransferController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TransferControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;
    @MockBean
    BankAccountService bankAccountService;
    @MockBean
    TransferService transferService;
    @MockBean
    TransferMapper transferMapper;

    private User currentUser;
    private User buddyToAdd;

    private TransferDto transferDto;

    @BeforeEach
    public void setup() {
        currentUser = getCurrentUser();
        Mockito.when(userService.getUserByEmail(currentUser.getEmail())).thenReturn(currentUser);
        Mockito.when(transferService.getTransfersDtoByBankAccount(currentUser.getPayMyBuddyBankAccount()))
                .thenReturn(Collections.emptyList());
        Mockito.when(transferMapper.convertListTransferDtoToPageOfTransferDto(
                        ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 1), 0));

        buddyToAdd = getBuddyToAdd();
        Mockito.when(userService.getUserByEmail(buddyToAdd.getEmail())).thenReturn(buddyToAdd);

        transferDto = getTransferDto();
    }


    @WithMockUser("currentUser@test")
    @Test
    public void showTransferPage_returnTransferView() throws Exception {
       /* User currentUser = getCurrentUser();
        Mockito.when(userService.getUserByEmail(currentUser.getEmail())).thenReturn(currentUser);
        Mockito.when(transferService.getTransfersDtoByBankAccount(currentUser.getPayMyBuddyBankAccount()))
                .thenReturn(Collections.emptyList());
        Mockito.when(transferMapper.convertListTransferDtoToPageOfTransferDto(
                        ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 1), 0));*/

        mockMvc.perform(MockMvcRequestBuilders.get("/transfer"))

                .andExpect(MockMvcResultMatchers.view().name("transfer"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("buddies"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("buddy"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("transfer"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("transfers"));
    }

    @WithMockUser("currentUser@test")
    @Test
    public void addBuddy_addConnexionToCurrentUserAndSaveIt_whenFormIsWellFilled() throws Exception {
        // User buddyToAdd = getBuddyToAdd();

        /////// COMMUN
       /* User currentUser = getCurrentUser();
        Mockito.when(userService.getUserByEmail(currentUser.getEmail())).thenReturn(currentUser);
        Mockito.when(transferService.getTransfersDtoByBankAccount(currentUser.getPayMyBuddyBankAccount()))
                .thenReturn(Collections.emptyList());
        Mockito.when(transferMapper.convertListTransferDtoToPageOfTransferDto(
                        ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 1), 0));*/
        ////////
        //Mockito.when(userService.getUserByEmail(buddyToAdd.getEmail())).thenReturn(buddyToAdd);

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

        Assertions.assertThat(currentUser.getUsersConnexions().get(0)).isEqualTo(buddyToAdd);
        Assertions.assertThat(buddyToAdd.getUsersConnected().get(0)).isEqualTo(currentUser);
        Mockito.verify(userService, Mockito.times(1)).saveUser(currentUser);
    }

    @WithMockUser("currentUser@test")
    @Test
    public void addBuddy_DisplayErrorMessages_whenFieldAreNotFilled() throws Exception {
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

    @WithMockUser("currentUser@test")
    @Test
    public void addBuddy_DisplayErrorMessages_whenFilledWithWrongEmail() throws Exception {

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

    public void addBuddy_DisplayErrorMessages_whenFilledWithCurrentUserEmail() throws Exception {

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

    public void addBuddy_DisplayErrorMessages_whenEmailIsAlreadyRegistered() throws Exception {

        // User buddyToAdd = getBuddyToAdd();

        //Buddy is already added:
        currentUser.addConnexion(buddyToAdd);

        mockMvc.perform(MockMvcRequestBuilders.post("/transfer/addBuddy")
                        .param("email", buddyToAdd.getEmail()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/transfer"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(CoreMatchers.containsString(buddyToAdd.getFirstName() + " "
                                + buddyToAdd.getLastName() + " is already add to your buddies!")))
                .andExpect(MockMvcResultMatchers.model().attributeExists("buddy"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("buddies"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("transfer"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("transfers"));
    }


    //TODO : a déplacer vers HomeControllerTest
    @Test
    @WithMockUser("currentUser@test")
    public void deleteBuddy_ShouldDeleteUsersBuddy() throws Exception {

        //Check that buddy is already added
        currentUser.addConnexion(buddyToAdd);
        assertThat(currentUser.getUsersConnexions().contains(buddyToAdd));
        assertThat(buddyToAdd.getUsersConnected().contains(currentUser));

        //Remove Buddy
        mockMvc.perform(MockMvcRequestBuilders.get("/transfer/deleteBuddy")
                        .param("email", buddyToAdd.getEmail()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/home"));

        //check that current user and his buddy are not connected anymore
        assertThat(currentUser.getUsersConnexions().isEmpty());
        assertThat(buddyToAdd.getUsersConnected().isEmpty());
    }
    ///

    @Test
    @DisplayName("/sendMoney should transfer money to selected user")
    @WithMockUser("currentUser@test")
    //
    public void sendMoney_CallTransferMethods_whenFormIsWellFilled() throws Exception {


        PersonalBankAccount masterBankAccount = getMasterBankAccount();
        Transfer transfer = new Transfer();
        PayMyBuddyBankAccount senderAccount = currentUser.getPayMyBuddyBankAccount();
        Mockito.when(transferMapper.convertTransferDtoToTransfer(
                        ArgumentMatchers.any(TransferDto.class), ArgumentMatchers.contains(currentUser.getEmail())))
                .thenReturn(transfer);

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

        Mockito.verify(transferService, Mockito.times(1))
                .takeTransferPercentage(transferDto.getAmount(), senderAccount);
        Mockito.verify(transferMapper, Mockito.times(1))
                .convertTransferDtoToTransfer(
                        ArgumentMatchers.any(TransferDto.class), ArgumentMatchers.contains(currentUser.getEmail()));
        Mockito.verify(bankAccountService, Mockito.times(1))
                .transfer(ArgumentMatchers.any(Transfer.class));
    }

    @Test
    @WithMockUser("currentUser@test")
    //sendMoney_CallTransferMethods_whenFormIsWellFilled
    public void sendMoney_DisplayErrorMessages_whithNoBuddySelected() throws Exception{
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
    @WithMockUser("currentUser@test")
    public void sendMoney_DisplayErrorMessages_whenWrongAmountIsSent() throws Exception{
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
    @WithMockUser("currentUser@test")
    public void sendMoney_DisplayErrorMessages_whenUserDontHaveEnoughMoney() throws Exception{
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


    //SETUP
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

    private User getBuddyToAdd() {
        User buddyToAdd = new User();
        buddyToAdd.setId(2);
        buddyToAdd.setEmail("buddy@test");
        buddyToAdd.setFirstName("tset");
        buddyToAdd.setLastName("test");
        buddyToAdd.setPassword("4321");

        PayMyBuddyBankAccount buddyToAddPayMyBuddyBankAccount = new PayMyBuddyBankAccount();
        buddyToAddPayMyBuddyBankAccount.setAccountBalance(500.00);
        buddyToAdd.setPayMyBuddyBankAccount(buddyToAddPayMyBuddyBankAccount);
        return buddyToAdd;
    }

    private TransferDto getTransferDto() {
        TransferDto transferDto = new TransferDto();
        transferDto.setAmount(50.00);
        transferDto.setBuddyUsername("buddy@test");
        transferDto.setDescription("Test");
        return transferDto;
    }

    private PersonalBankAccount getMasterBankAccount() {
        PersonalBankAccount masterBankAccount = new PersonalBankAccount();
        masterBankAccount.setAccountBalance(10000);
        masterBankAccount.setIban("666");
        return masterBankAccount;
    }
}

