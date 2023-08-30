package com.paymybuddy.paymybuddysapp.mapper;

import com.paymybuddy.paymybuddysapp.dto.TransferDto;
import com.paymybuddy.paymybuddysapp.model.Transfer;
import com.paymybuddy.paymybuddysapp.model.User;
import com.paymybuddy.paymybuddysapp.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class TransferMapper {

    private UserService userService;

    public TransferMapper(UserService userService){
        this.userService = userService;
    }

    //TODO : La classe transferMapper va contenir les méthodes necessaires
    // pour convertir les ExternalTransferDto et les TransferDto en objet Transfer.
    // J'ajoute les méthodes de conversion au fur et a mesure que je rencontre un besoin
    // Je fais les test en suivant
   public Transfer convertInternalTransferDtoToTransfer(TransferDto transferDto, String currentUsername ) {

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
   //TODO: ajouter un traitement au cas où les champs de transferDto sont Null
}