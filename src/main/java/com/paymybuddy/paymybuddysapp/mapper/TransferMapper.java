package com.paymybuddy.paymybuddysapp.mapper;

import com.paymybuddy.paymybuddysapp.dto.TransferDto;
import com.paymybuddy.paymybuddysapp.model.Transfer;
import com.paymybuddy.paymybuddysapp.model.User;
import com.paymybuddy.paymybuddysapp.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class TransferMapper {

    private UserService userService;

    public TransferMapper(UserService userService) {
        this.userService = userService;
    }

    //TODO : La classe transferMapper va contenir les méthodes necessaires
    // pour convertir les ExternalTransferDto et les TransferDto en objet Transfer.
    // J'ajoute les méthodes de conversion au fur et a mesure que je rencontre un besoin
    // Je fais les test en suivant
    public Transfer convertTransferDtoToTransfer(TransferDto transferDto, String currentUsername) {

        User senderUser = userService.getUserByEmail(currentUsername);
        User receivingUser = userService.getUserByEmail(transferDto.getBuddyUsername());

        Transfer transfer = new Transfer();
        transfer.setAmount(transferDto.getAmount());
        transfer.setSenderAccount(senderUser.getPayMyBuddyBankAccount());
        transfer.setRecipientAccount(receivingUser.getPayMyBuddyBankAccount());
        transfer.setDescription(transferDto.getDescription());
        transfer.setDate(transferDto.getDate());

        return transfer;
    }
    //TODO: ajouter un traitement au cas où les champs de transferDto sont Null???

    public TransferDto convertTransferToTransferDto(Transfer transfer, boolean isSentByCurrentUser) {

        TransferDto transferDto = new TransferDto();

        transferDto.setAmount(transfer.getAmount());
        transferDto.setDescription(transfer.getDescription());
        transferDto.setDate(transfer.getDate());

        if (isSentByCurrentUser == true) { //For a sent transfer, the buddy is the receiving account
            User buddy = userService.getUserByBankAccount(transfer.getRecipientAccount());
            transferDto.setBuddyUsername(buddy.getEmail());
            transferDto.setDisplayAmount("-" + transfer.getAmount() + "€");
        } else {//For a transfer received, the buddy is the sender account
            User buddy = userService.getUserByBankAccount(transfer.getSenderAccount());
            transferDto.setBuddyUsername(buddy.getEmail());
            transferDto.setDisplayAmount("+" + transfer.getAmount() + "€");
        }
        return transferDto;
    }
}