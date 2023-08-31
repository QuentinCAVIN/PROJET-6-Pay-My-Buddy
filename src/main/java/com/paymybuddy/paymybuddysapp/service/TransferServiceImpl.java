package com.paymybuddy.paymybuddysapp.service;

import com.paymybuddy.paymybuddysapp.dto.TransferDto;
import com.paymybuddy.paymybuddysapp.mapper.TransferMapper;
import com.paymybuddy.paymybuddysapp.model.*;
import com.paymybuddy.paymybuddysapp.repository.TransferRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransferServiceImpl implements TransferService {


    private TransferRepository transferRepository;

    private TransferMapper transferMapper; //TODO à supprimer du constructeur si je change l'emplacement de getTransfersDtoFromPayMyBuddyBankAccountByUser

    private DateProvider dateProvider;

    public TransferServiceImpl(TransferRepository transferRepository, DateProvider dateProvider, TransferMapper transferMapper) {
        this.transferRepository = transferRepository;
        this.dateProvider = dateProvider;
        this.transferMapper =transferMapper;
    }
 // Quand l'attibut dateProvider n'est pas injecté, le test ne fait pas appelle au mock mais a l'instance réel de
 //     DateProvider
    public void createNewTransfer(Transfer transfer) {

        //Comme ça : DateProvider dateProvider= new DateProvider
        String transferDate = dateProvider.todaysDate();

        transfer.setDate(transferDate);
        transferRepository.save(transfer);
        //TODO : Ne faut il pas faire un BankAccount addSentTransfer et addReceivedTransfer?
        // A priori non, pas besoin, JPA fait le café. En fin de projet supprimer toutes les méthodes utilitaire et
        // voir si le café coule toujours
    }

    public List<TransferDto> getTransfersDtoByBankAccount(BankAccount bankAccount) {

        List<TransferDto> transfersDtoByBankAccount = new ArrayList<>();

        List<Transfer> transferSent = bankAccount.getSentTransfers();
        transferSent.forEach(transfer -> {
                    TransferDto transferDto = transferMapper.convertTransferToTransferDto(transfer,true);
                            transfersDtoByBankAccount.add(transferDto);
                });

        List<Transfer> transferReceived = bankAccount.getReceivedTransfers();
        transferReceived.forEach(transfer -> {
            TransferDto transferDto = transferMapper.convertTransferToTransferDto(transfer,false);
            transfersDtoByBankAccount.add(transferDto);
        });
        return transfersDtoByBankAccount;
    }
}
