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
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<TransferDto> list;

        if (listToConvert.size() < startItem) {
            list = Collections.emptyList();

        } else {
            int toIndex = Math.min(startItem + pageSize, listToConvert.size());
            list = listToConvert.subList(startItem, toIndex);
        }
        Page<TransferDto> transferDtoPage
                = new PageImpl<TransferDto>(list, PageRequest.of(currentPage, pageSize), listToConvert.size());

        return transferDtoPage;
    }
}