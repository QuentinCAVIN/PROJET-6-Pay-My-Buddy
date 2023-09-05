package com.paymybuddy.paymybuddysapp.service;

import com.paymybuddy.paymybuddysapp.model.*;
import com.paymybuddy.paymybuddysapp.repository.PayMyBuddyBankAccountRepository;
import com.paymybuddy.paymybuddysapp.repository.PersonalBankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BankAccountService {

    void transfer(Transfer transfer);

    void saveBankAccount(BankAccount bankAccount);

    //TODO TEST ci dessous
    void linkNewPersonalBankAccount(PersonalBankAccount personalBankAccount, User user);
    PersonalBankAccount getMasterBankAccount();
}

