package com.paymybuddy.paymybuddysapp.unittest;

import com.paymybuddy.paymybuddysapp.dto.TransferDto;
import com.paymybuddy.paymybuddysapp.dto.UserDto;
import com.paymybuddy.paymybuddysapp.mapper.TransferMapper;
import com.paymybuddy.paymybuddysapp.model.*;
import com.paymybuddy.paymybuddysapp.repository.TransferRepository;
import com.paymybuddy.paymybuddysapp.repository.UserRepository;
import com.paymybuddy.paymybuddysapp.service.DateProvider;
import com.paymybuddy.paymybuddysapp.service.TransferService;
import com.paymybuddy.paymybuddysapp.service.TransferServiceImpl;
import com.paymybuddy.paymybuddysapp.service.UserServiceImpl;
import net.bytebuddy.matcher.ElementMatcher;
import org.assertj.core.api.Assertions;
import org.glassfish.jaxb.core.v2.TODO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.DateFormat;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class TransferServiceImplTest {

    @Mock
    private TransferRepository transferRepository;
    @Mock
    private DateProvider dateProvider;
    @Mock
    private TransferMapper transferMapper;
    @InjectMocks
    private TransferServiceImpl transferService;


    private static User dummy;
    private static PayMyBuddyBankAccount dummyAccount;
    private static PayMyBuddyBankAccount ymmudAccount;
    private static PersonalBankAccount dummyPersonnalAccount;
    private static User ymmud;

    private static Transfer transferSendFromPayMyBuddyAccount1;
    private static Transfer transferSendFromPayMyBuddyAccount2;
    private static Transfer transferReceived;

    @BeforeEach
    public void setup() {
        dummy = new User();
        dummy.setId(1);
        dummy.setEmail("dum@my");
        dummy.setPassword("1234");
        dummy.setFirstName("Dum");
        dummy.setLastName("MY");
        dummy.setUsersConnexions(Arrays.asList(ymmud));
        dummy.setUsersConnected(null);
        dummyAccount = new PayMyBuddyBankAccount();
        dummyAccount.setId(1);
        dummyAccount.setAccountBalance(1234);
        dummy.setPayMyBuddyBankAccount(dummyAccount);
        dummyPersonnalAccount = new PersonalBankAccount();
        dummyPersonnalAccount.setId(5);
        dummyPersonnalAccount.setIban("123456789");
        dummyPersonnalAccount.setAccountBalance(12345);
        dummy.setPersonalBankAccount(dummyPersonnalAccount);

        ymmud = new User();
        ymmud.setId(2);
        ymmud.setEmail("ym@mud");
        ymmud.setPassword("4321");
        ymmud.setFirstName("muD");
        ymmud.setLastName("YM");
        ymmud.setUsersConnexions(null);
        ymmud.setUsersConnected(Arrays.asList(dummy));
        ymmud.setPayMyBuddyBankAccount(new PayMyBuddyBankAccount());
        ymmudAccount = new PayMyBuddyBankAccount();
        ymmudAccount.setId(2);
        ymmudAccount.setAccountBalance(4321);
        ymmud.setPayMyBuddyBankAccount(ymmudAccount);

        transferSendFromPayMyBuddyAccount1 = new Transfer();
        transferSendFromPayMyBuddyAccount1.setId(1);
        transferSendFromPayMyBuddyAccount1.setAmount(69);
        transferSendFromPayMyBuddyAccount1.setSenderAccount(dummyAccount);
        transferSendFromPayMyBuddyAccount1.setRecipientAccount(ymmudAccount);
        transferSendFromPayMyBuddyAccount1.setDescription("for my ymmud");
        transferSendFromPayMyBuddyAccount1.setDate("30/08/2023");

        transferSendFromPayMyBuddyAccount2 = new Transfer();
        transferSendFromPayMyBuddyAccount2.setId(2);
        transferSendFromPayMyBuddyAccount2.setAmount(33);
        transferSendFromPayMyBuddyAccount2.setSenderAccount(dummyAccount);
        transferSendFromPayMyBuddyAccount2.setRecipientAccount(ymmudAccount);
        transferSendFromPayMyBuddyAccount2.setDescription("for my ymmud again");
        transferSendFromPayMyBuddyAccount2.setDate("31/08/2023");

        dummyAccount.setSentTransfers(Arrays.asList(transferSendFromPayMyBuddyAccount1,transferSendFromPayMyBuddyAccount2));

        transferReceived = new Transfer();
        transferReceived.setId(3);
        transferReceived.setAmount(10);
        transferReceived.setSenderAccount(ymmudAccount);
        transferReceived.setRecipientAccount(dummyAccount);
        transferReceived.setDescription("for my dummy");
        transferReceived.setDate("30/08/2023");

        dummyAccount.setReceivedTransfers(Arrays.asList(transferReceived));

        /*PersonalBankAccount personalBankAccountOfdummy = new PersonalBankAccount();
        personalBankAccountOfdummy.setId(5);
        personalBankAccountOfdummy.setAccountBalance(1000);
        personalBankAccountOfdummy.setIban("11111111");

*/
    }



    @Test
    public void createNewTransferTest() {

        PayMyBuddyBankAccount senderAccount = new PayMyBuddyBankAccount();
        senderAccount.setAccountBalance(500);
        PayMyBuddyBankAccount recipientAccount = new PayMyBuddyBankAccount();
        recipientAccount.setAccountBalance(500);

        // Transfer generated from the form and the senMoney method
        Transfer transfer = new Transfer();
        transfer.setId(1);
        transfer.setSenderAccount(senderAccount);
        transfer.setRecipientAccount(recipientAccount);
        transfer.setAmount(50);
        transfer.setDescription("test transfer");

        String transferDate = "26/08/2023";

        Mockito.when(dateProvider.todaysDate()).thenReturn(transferDate);

        transferService.createNewTransfer(transfer);

        Mockito.verify(transferRepository, Mockito.times(1)).save(transfer);
        Assertions.assertThat(transfer.getDate()).isEqualTo(transferDate);
    }

    @Test
    public void getTransfersDtoByBankAccountTest() {
        TransferDto transferDtoSent1 = new TransferDto();
        transferDtoSent1.setDisplayAmount("10");
        transferDtoSent1.setBuddyUsername("transfer1");
        transferDtoSent1.setDate("01/01/01");
        transferDtoSent1.setAmount(10);
        transferDtoSent1.setDescription("transfer1");

        TransferDto transferDtoSent2 = new TransferDto();
        transferDtoSent1.setDisplayAmount("11");
        transferDtoSent1.setBuddyUsername("transfer2");
        transferDtoSent1.setDate("02/01/01");
        transferDtoSent1.setAmount(11);
        transferDtoSent1.setDescription("transfer2");

        TransferDto transferDtoReceived = new TransferDto();
        transferDtoReceived.setDisplayAmount("12");
        transferDtoReceived.setBuddyUsername("transfer received");
        transferDtoReceived.setDate("03/01/01");
        transferDtoReceived.setAmount(12);
        transferDtoReceived.setDescription("transfer received");

        List<TransferDto> listToCompare = Arrays.asList(transferDtoSent1,transferDtoSent2,transferDtoReceived);

        Mockito.when(transferMapper.convertTransferToTransferDto(transferSendFromPayMyBuddyAccount1,true))
                .thenReturn(transferDtoSent1);
        Mockito.when(transferMapper.convertTransferToTransferDto(transferSendFromPayMyBuddyAccount2,true))
                .thenReturn(transferDtoSent2);
        Mockito.when(transferMapper.convertTransferToTransferDto(transferReceived,false))
                .thenReturn(transferDtoReceived);

        List<TransferDto> transfersDto = transferService.getTransfersDtoByBankAccount(dummyAccount);

        Assertions.assertThat(transfersDto).containsExactlyInAnyOrderElementsOf(listToCompare);
        Mockito.verify(transferMapper,Mockito.times(2))
                .convertTransferToTransferDto(ArgumentMatchers.any(Transfer.class),ArgumentMatchers.eq(true));
        Mockito.verify(transferMapper,Mockito.times(1))
                .convertTransferToTransferDto(ArgumentMatchers.any(Transfer.class),ArgumentMatchers.eq(false));
    }
}