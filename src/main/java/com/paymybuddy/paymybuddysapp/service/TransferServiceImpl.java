package com.paymybuddy.paymybuddysapp.service;

import com.paymybuddy.paymybuddysapp.model.*;
import com.paymybuddy.paymybuddysapp.repository.TransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

        //TODO: Comme ça : DateProvider dateProvider= new DateProvider
        String transferDate = dateProvider.todaysDate();

        transfer.setDate(transferDate);
        transferRepository.save(transfer);
    }
    //TODO: CI DESSOUS EN CHANTIER + A TESTER. Essayer de modifier la structure du tansferDto pour afficher les transfers

   /* public List<Transfer> getAllTransfersToPayMyBuddyBankAccountByUser (User user){

        List<Transfer> allTransfersToPayMyBuddyBankAccountByUser = new ArrayList<>();

        PayMyBuddyBankAccount payMyBuddyBankAccount = user.getPayMyBuddyBankAccount();
        List<Transfer> payMyBuddyBankAccountTransferSent = payMyBuddyBankAccount.getSentTransfers();
        payMyBuddyBankAccountTransferSent.forEach(transfer -> allTransfersToPayMyBuddyBankAccountByUser.add(transfer));
        List<Transfer> payMyBuddyBankAccountTransferReceived = payMyBuddyBankAccount.getReceivedTransfers();
        payMyBuddyBankAccountTransferReceived.forEach(transfer -> allTransfersToPayMyBuddyBankAccountByUser.add(transfer));

        return allTransfersToPayMyBuddyBankAccountByUser


        PersonalBankAccount personalBankAccount = user.getPayMyBuddyBankAccount().getPersonalBankAccount();
        if (personalBankAccount != null) {
            List<Transfer> personalBankAccountTransferSent = personalBankAccount.getSentTransfers();
            personalBankAccountTransferSent.forEach(transfer -> allTransfersSentByUser.add(transfer));
        }
        return allTransfersSentByUser;

        // je dois obtenir un compte bancaire à partir d'un utilisateur puis un transfer à partir de ce compte bancaire.
        // A cause de l'héritage, je ne peux pas utiliser les mots clé JPA bankAccountRepository.findByUser car
        ///il y'a deux bankRepository distinct
    }
    public List<Transfer> getAllTransferReceivedByUser (User user){

        List<Transfer> allTransfersReceivedByUser = new ArrayList<>();

        PayMyBuddyBankAccount payMyBuddyBankAccount = user.getPayMyBuddyBankAccount();
        List<Transfer> payMyBuddyBankAccountTransferReceived = payMyBuddyBankAccount.getReceivedTransfers();
        payMyBuddyBankAccountTransferReceived.forEach(transfer -> allTransfersReceivedByUser.add(transfer));


        PersonalBankAccount personalBankAccount = user.getPayMyBuddyBankAccount().getPersonalBankAccount();
        if (personalBankAccount != null) {
            List<Transfer> personalBankAccountTransferReceived = personalBankAccount.getReceivedTransfers();
            personalBankAccountTransferReceived.forEach(transfer -> allTransfersReceivedByUser.add(transfer));
        }
        return allTransfersReceivedByUser;
    }*/
}
