package com.paymybuddy.paymybuddysapp.service;

import com.paymybuddy.paymybuddysapp.model.*;;
import org.springframework.stereotype.Service;

@Service
public interface BankAccountService {

    void transfer(Transfer transfer);

    void saveBankAccount(BankAccount bankAccount);

    void linkNewPersonalBankAccount(PersonalBankAccount personalBankAccount, User user);
    PersonalBankAccount getMasterBankAccount();
}

