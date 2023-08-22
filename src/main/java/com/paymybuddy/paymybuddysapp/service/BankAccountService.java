package com.paymybuddy.paymybuddysapp.service;

import com.paymybuddy.paymybuddysapp.model.BankAccount;
import com.paymybuddy.paymybuddysapp.model.PayMyBuddyBankAccount;
import com.paymybuddy.paymybuddysapp.model.PersonalBankAccount;
import com.paymybuddy.paymybuddysapp.model.Transfer;
import com.paymybuddy.paymybuddysapp.repository.PayMyBuddyBankAccountRepository;
import com.paymybuddy.paymybuddysapp.repository.PersonalBankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountService {
    @Autowired
    private PayMyBuddyBankAccountRepository payMyBuddyBankAccountRepository;
    @Autowired
    private PersonalBankAccountRepository personalBankAccountRepository;

    private Transfer transfer;

    public void transfer(BankAccount senderAccount,
                         BankAccount recipientAccount, double amount) {
        double senderAccountBalance = senderAccount.getAccountBalance();
        double recipientAccountBalance = recipientAccount.getAccountBalance();

        senderAccount.setAccountBalance(senderAccountBalance - amount);
        recipientAccount.setAccountBalance(recipientAccountBalance + amount);

        saveBankAccount(senderAccount);
        saveBankAccount(recipientAccount);
    }

    public void transfer(Transfer transfer) {

        BankAccount senderAccount = transfer.getSenderAccount();
        BankAccount recipientAccount = transfer.getRecipientAccount();
        double amount = transfer.getAmount();

        double senderAccountBalance = transfer.getSenderAccount().getAccountBalance();
        double recipientAccountBalance = transfer.getRecipientAccount().getAccountBalance();

        senderAccount.setAccountBalance(senderAccountBalance - amount);
        recipientAccount.setAccountBalance(recipientAccountBalance + amount);

        saveBankAccount(senderAccount);
        saveBankAccount(recipientAccount);
    }

    public void saveBankAccount(BankAccount bankAccount) {
        if (bankAccount instanceof PayMyBuddyBankAccount) {
            payMyBuddyBankAccountRepository.save((PayMyBuddyBankAccount) bankAccount);
            //utilisation d'un cast pour pouvoir utiliser un objet BankAccount avec un PayMyBuddyBankAccount

        } else if (bankAccount instanceof PersonalBankAccount) {
            personalBankAccountRepository.save((PersonalBankAccount) bankAccount);
        }
        //TODO: faire les tests unitaires
    }
}
