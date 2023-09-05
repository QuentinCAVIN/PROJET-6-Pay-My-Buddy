package com.paymybuddy.paymybuddysapp.mapper;

import com.paymybuddy.paymybuddysapp.dto.BankAccountDto;
import com.paymybuddy.paymybuddysapp.model.BankAccount;
import com.paymybuddy.paymybuddysapp.model.PayMyBuddyBankAccount;
import com.paymybuddy.paymybuddysapp.model.PersonalBankAccount;

//TODO: A TESTER
public class BankAccountMapper {
    public static PersonalBankAccount convertBankAccountDtoToPersonalBankAccount(BankAccountDto bankAccountDto){
        PersonalBankAccount personalBankAccount = new PersonalBankAccount();

        personalBankAccount.setAccountBalance(bankAccountDto.getAccountBalance());
        personalBankAccount.setIban(bankAccountDto.getIban());

        return personalBankAccount;
    }

    public static BankAccountDto convertPersonalBankAccountToBankAccountDto(PersonalBankAccount personalBankAccount){
        BankAccountDto bankAccountDto = new BankAccountDto();

        bankAccountDto.setIban(personalBankAccount.getIban());
        bankAccountDto.setAccountBalance(personalBankAccount.getAccountBalance());

        return bankAccountDto; // TODO A supprimer quand je saurais que l'autre méthode est fonctionelle (ci dessous)
    }

    public static BankAccountDto convertBankAccountToBankAccountDto(BankAccount bankAccount){
        BankAccountDto bankAccountDto = new BankAccountDto();
        if (bankAccount instanceof PayMyBuddyBankAccount){
            bankAccountDto.setAccountBalance(bankAccount.getAccountBalance());
        }
        else if (bankAccount instanceof PersonalBankAccount){

            PersonalBankAccount personalBankAccount = (PersonalBankAccount) bankAccount;

            bankAccountDto.setIban(personalBankAccount.getIban());
            bankAccountDto.setAccountBalance(personalBankAccount.getAccountBalance());
        }
        return bankAccountDto;
    }
}