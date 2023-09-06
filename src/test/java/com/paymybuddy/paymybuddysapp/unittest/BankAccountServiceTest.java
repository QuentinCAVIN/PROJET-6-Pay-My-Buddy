package com.paymybuddy.paymybuddysapp.unittest;

import com.paymybuddy.paymybuddysapp.model.PayMyBuddyBankAccount;
import com.paymybuddy.paymybuddysapp.model.PersonalBankAccount;
import com.paymybuddy.paymybuddysapp.model.Transfer;
import com.paymybuddy.paymybuddysapp.repository.BankAccountRepository;
import com.paymybuddy.paymybuddysapp.repository.PayMyBuddyBankAccountRepository;
import com.paymybuddy.paymybuddysapp.repository.PersonalBankAccountRepository;
import com.paymybuddy.paymybuddysapp.service.BankAccountServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BankAccountServiceTest {

    @Mock
    PayMyBuddyBankAccountRepository payMyBuddyBankAccountRepository;
    @Mock
    PersonalBankAccountRepository personalBankAccountRepository;
    @Mock
    BankAccountRepository bankAccountRepository;

    @InjectMocks
    BankAccountServiceImpl bankAccountService;

    Transfer transferPayMyBuddyAccountToPayMyBuddyAccount;
    Transfer transferPersonalBankAccountToPayMyBuddyAccount;
    Transfer transferPayMyBuddyAccountToPersonalBankAccount;

    PayMyBuddyBankAccount payMyBuddySenderBankAccount;
    PayMyBuddyBankAccount payMyBuddyRecipientBankAccount;
    PersonalBankAccount personalBankAccount;

    @BeforeEach
    public void setup() {

        payMyBuddySenderBankAccount = new PayMyBuddyBankAccount();
        payMyBuddySenderBankAccount.setAccountBalance(100);

        payMyBuddyRecipientBankAccount = new PayMyBuddyBankAccount();
        payMyBuddyRecipientBankAccount.setAccountBalance(200);

        personalBankAccount = new PersonalBankAccount();
        personalBankAccount.setAccountBalance(300);

        transferPayMyBuddyAccountToPayMyBuddyAccount = new Transfer();
        transferPayMyBuddyAccountToPayMyBuddyAccount.setSenderAccount(payMyBuddySenderBankAccount);
        transferPayMyBuddyAccountToPayMyBuddyAccount.setRecipientAccount(payMyBuddyRecipientBankAccount);
        transferPayMyBuddyAccountToPayMyBuddyAccount.setAmount(50);

        transferPersonalBankAccountToPayMyBuddyAccount = new Transfer();
        transferPersonalBankAccountToPayMyBuddyAccount.setSenderAccount(personalBankAccount);
        transferPersonalBankAccountToPayMyBuddyAccount.setRecipientAccount(payMyBuddyRecipientBankAccount);
        transferPersonalBankAccountToPayMyBuddyAccount.setAmount(100);

        transferPayMyBuddyAccountToPersonalBankAccount = new Transfer();
        transferPayMyBuddyAccountToPersonalBankAccount.setSenderAccount(payMyBuddySenderBankAccount);
        transferPayMyBuddyAccountToPersonalBankAccount.setRecipientAccount(personalBankAccount);
        transferPayMyBuddyAccountToPersonalBankAccount.setAmount(50.50);
    }

    @Test
    public void saveBankAccountWithPayMyBuddyAccountTest() {
        bankAccountService.saveBankAccount(payMyBuddySenderBankAccount);
        Mockito.verify(payMyBuddyBankAccountRepository, Mockito.times(1))
                .save(ArgumentMatchers.any(PayMyBuddyBankAccount.class));
    }

    @Test
    public void saveBankAccountWithPersonalAccountTest() {
        bankAccountService.saveBankAccount(personalBankAccount);
        Mockito.verify(personalBankAccountRepository, Mockito.times(1))
                .save(ArgumentMatchers.any(PersonalBankAccount.class));
    }

    @Test
    public void transferPayMyBuddyAccountToPayMyBuddyAccountTest() {

        bankAccountService.transfer(transferPayMyBuddyAccountToPayMyBuddyAccount);

        Assertions.assertThat(payMyBuddySenderBankAccount.getAccountBalance()).isEqualTo(100 - 50);
        Assertions.assertThat(payMyBuddyRecipientBankAccount.getAccountBalance()).isEqualTo(200 + 50);

        Mockito.verify(payMyBuddyBankAccountRepository, Mockito.times(1))
                .save(payMyBuddySenderBankAccount);
        Mockito.verify(payMyBuddyBankAccountRepository, Mockito.times(1))
                .save(payMyBuddyRecipientBankAccount);
    }

    @Test
    public void transferPersonalAccountToPayMyBuddyAccountTest() {

        bankAccountService.transfer(transferPersonalBankAccountToPayMyBuddyAccount);

        Assertions.assertThat(personalBankAccount.getAccountBalance()).isEqualTo(300 - 100);
        Assertions.assertThat(payMyBuddyRecipientBankAccount.getAccountBalance()).isEqualTo(200 + 100);

        Mockito.verify(personalBankAccountRepository, Mockito.times(1))
                .save(personalBankAccount);
        Mockito.verify(payMyBuddyBankAccountRepository, Mockito.times(1))
                .save(payMyBuddyRecipientBankAccount);
    }

    @Test
    public void transferPayMyBuddyAccountToPersonalBankAccountTest() {

        bankAccountService.transfer(transferPayMyBuddyAccountToPersonalBankAccount);

        Assertions.assertThat(payMyBuddySenderBankAccount.getAccountBalance()).isEqualTo(100 - 50.50);
        Assertions.assertThat(personalBankAccount.getAccountBalance()).isEqualTo(300 + 50.50);

        Mockito.verify(payMyBuddyBankAccountRepository, Mockito.times(1))
                .save(payMyBuddySenderBankAccount);
        Mockito.verify(personalBankAccountRepository, Mockito.times(1))
                .save(personalBankAccount);
    }

    @Test
    public void getMasterBankAccountTest() {
        PersonalBankAccount masterBankAccount = new PersonalBankAccount();
        masterBankAccount.setAccountBalance(10000);
        masterBankAccount.setIban("666");
        Mockito.when(bankAccountRepository.findByIban("666")).thenReturn(masterBankAccount);

        PersonalBankAccount personalBankAccount = bankAccountService.getMasterBankAccount();

        Mockito.verify(bankAccountRepository, Mockito.times(1)).findByIban("666");
        Assertions.assertThat(personalBankAccount).isEqualTo(masterBankAccount);
    }
}
