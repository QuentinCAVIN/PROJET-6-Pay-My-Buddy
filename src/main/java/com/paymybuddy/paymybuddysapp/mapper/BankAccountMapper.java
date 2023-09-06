package com.paymybuddy.paymybuddysapp.mapper;

import com.paymybuddy.paymybuddysapp.dto.BankAccountDto;
import com.paymybuddy.paymybuddysapp.model.BankAccount;
import com.paymybuddy.paymybuddysapp.model.PayMyBuddyBankAccount;
import com.paymybuddy.paymybuddysapp.model.PersonalBankAccount;

public class BankAccountMapper {
    public static PersonalBankAccount convertBankAccountDtoToPersonalBankAccount(BankAccountDto bankAccountDto) {
        PersonalBankAccount personalBankAccount = new PersonalBankAccount();

        personalBankAccount.setAccountBalance(bankAccountDto.getAccountBalance());
        personalBankAccount.setIban(bankAccountDto.getIban());

        return personalBankAccount;
    }

    public static BankAccountDto convertBankAccountToBankAccountDto(BankAccount bankAccount) {
        BankAccountDto bankAccountDto = new BankAccountDto();
        if (bankAccount instanceof PayMyBuddyBankAccount) {
            bankAccountDto.setAccountBalance(Math.round((bankAccount.getAccountBalance() * 100.00)) / 100.00);
        } else if (bankAccount instanceof PersonalBankAccount) {

            PersonalBankAccount personalBankAccount = (PersonalBankAccount) bankAccount;

            bankAccountDto.setIban(personalBankAccount.getIban());
            bankAccountDto.setAccountBalance(Math.round((personalBankAccount.getAccountBalance() * 100.00)) / 100.00);
        }
        return bankAccountDto;
    }
}