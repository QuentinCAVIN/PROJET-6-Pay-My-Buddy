package com.paymybuddy.paymybuddysapp.unittest;

import com.paymybuddy.paymybuddysapp.model.PayMyBuddyBankAccount;
import com.paymybuddy.paymybuddysapp.model.Transfer;
import com.paymybuddy.paymybuddysapp.repository.TransferRepository;
import com.paymybuddy.paymybuddysapp.repository.UserRepository;
import com.paymybuddy.paymybuddysapp.service.DateProvider;
import com.paymybuddy.paymybuddysapp.service.TransferService;
import com.paymybuddy.paymybuddysapp.service.TransferServiceImpl;
import com.paymybuddy.paymybuddysapp.service.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class TransferServiceImplTest {

    @Mock
    private TransferRepository transferRepository;


    @Mock
    private DateProvider dateProvider;

    @InjectMocks
    private TransferServiceImpl transferService;


    @Test
    public void createNewTransferTest() {

        PayMyBuddyBankAccount senderAccount = new PayMyBuddyBankAccount();
        senderAccount.setAccountBalance(500);
        PayMyBuddyBankAccount recipientAccount = new PayMyBuddyBankAccount();
        recipientAccount.setAccountBalance(500);

        // Transfer generated from the form and the senMoney method
        Transfer transfer = new Transfer();
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
}