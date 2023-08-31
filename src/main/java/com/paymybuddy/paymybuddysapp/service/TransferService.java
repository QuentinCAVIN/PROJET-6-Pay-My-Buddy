package com.paymybuddy.paymybuddysapp.service;

import com.paymybuddy.paymybuddysapp.dto.TransferDto;
import com.paymybuddy.paymybuddysapp.model.BankAccount;
import com.paymybuddy.paymybuddysapp.model.Transfer;

import java.util.List;

public interface TransferService {
    public void createNewTransfer (Transfer transfer);

    public List<TransferDto> getTransfersDtoByBankAccount(BankAccount bankAccount);
}
