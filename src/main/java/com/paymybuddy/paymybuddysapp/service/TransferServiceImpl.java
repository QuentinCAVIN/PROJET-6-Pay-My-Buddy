package com.paymybuddy.paymybuddysapp.service;

import com.paymybuddy.paymybuddysapp.model.Transfer;
import com.paymybuddy.paymybuddysapp.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
@Service
public class TransferServiceImpl implements TransferService {


    private TransferRepository transferRepository;


    private DateProvider dateProvider;

    public TransferServiceImpl(TransferRepository transferRepository, DateProvider dateProvider) {
        this.transferRepository = transferRepository;
        this.dateProvider = dateProvider;
    }
 // TODO : Quand l'attibut dateProvider n'est pas injecté, le test ne fait pas apelle au mock mais a l'instance réel de
 //     DateProvider
    public void createNewTransfer(Transfer transfer) {

        String transferDate = dateProvider.todaysDate();

        transfer.setDate(transferDate);
        transferRepository.save(transfer);
    }
}
