package com.paymybuddy.paymybuddysapp.mapper;

import com.paymybuddy.paymybuddysapp.dto.TransferDto;
import com.paymybuddy.paymybuddysapp.model.Transfer;
import com.paymybuddy.paymybuddysapp.model.User;
import com.paymybuddy.paymybuddysapp.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

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

    public Page<TransferDto> convertListTransferDtoToPageOfTransferDto(Pageable pageable,
                                                                       List<TransferDto> listToConvert) {
    // L'objet Pageable passé en paramètre est construit dans le controller: PageRequest.of(currentPage - 1, pageSize)
    //Il va servir a définir la page qui sera affiché et le nombre d'éléments par pages.

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize; //sert a afficher le premier élément de la page, page 0 = element 0 (=1er)
        List<TransferDto> list;

        if (listToConvert.size() < startItem) {// Vérifie qu'il y a assez d'éléments pour remplir une page
            list = Collections.emptyList(); //si non, la methode renverra finalement une list<Object>
            // plutot qu'un Page<Object>
        } else {
            int toIndex = Math.min(startItem + pageSize, listToConvert.size());//Calcul l'indice de fin
            list = listToConvert.subList(startItem,toIndex);
            //list est initialisé avec les éléments appropriés en utilisant subList pour extraire une sous-liste
            // de listToConvert en partant de l'indice de départ (startItem) jusqu'à toIndex, l'indice de fin,
            // qui est le début de la page suivante ou la fin de la liste, selon lequel est plus petit.
        }
        Page<TransferDto> transferDtoPage
                = new PageImpl<TransferDto>(list, PageRequest.of(currentPage, pageSize), listToConvert.size());
        //Cette ligne crée un objet PageImpl (Page est une interface) a partir de la sous-list,
        //d'un objet PageRequest (composé du numéro de page et du nombre d'objet par page) et de la taille total de
        //la liste à convertir.

        return transferDtoPage;
    }

    //Exemple pagination:
// https://www.baeldung.com/spring-thymeleaf-pagination
//https://www.baeldung.com/spring-data-jpa-convert-list-page

//Avec un Repository :
//https://www.baeldung.com/spring-data-jpa-pagination-sorting
}