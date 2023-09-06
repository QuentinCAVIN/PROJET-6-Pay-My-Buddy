package com.paymybuddy.paymybuddysapp.unittest;

import com.paymybuddy.paymybuddysapp.dto.TransferDto;
import com.paymybuddy.paymybuddysapp.mapper.TransferMapper;
import com.paymybuddy.paymybuddysapp.model.PayMyBuddyBankAccount;
import com.paymybuddy.paymybuddysapp.model.Transfer;
import com.paymybuddy.paymybuddysapp.model.User;
import com.paymybuddy.paymybuddysapp.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TransferMapperTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private TransferMapper transferMapper;

    private static User senderUser;
    private static User receivingUser;
    private static TransferDto transferDto;
    private static PayMyBuddyBankAccount senderAccount;
    private static PayMyBuddyBankAccount recipientAccount;
    private static Transfer transfer;

    @BeforeAll
    public static void setup() {

        senderAccount = new PayMyBuddyBankAccount();
        senderAccount.setAccountBalance(500);
        recipientAccount = new PayMyBuddyBankAccount();
        recipientAccount.setAccountBalance(500);

        senderUser = new User();
        senderUser.setEmail("roger@roger");
        receivingUser = new User();
        receivingUser.setEmail("roberta@tourbillon");
        senderUser.setPayMyBuddyBankAccount(senderAccount);
        receivingUser.setPayMyBuddyBankAccount(recipientAccount);

        transfer = new Transfer();
        transfer.setId(1);
        transfer.setAmount(69);
        transfer.setSenderAccount(senderAccount);
        transfer.setRecipientAccount(recipientAccount);
        transfer.setDescription("for my ymmud");
        transfer.setDate("30/08/2023");

        transferDto = new TransferDto();
        transferDto.setAmount(50.00);
        transferDto.setBuddyUsername("buddy@test");
        transferDto.setDescription("Test");
        transferDto.setDate("23/01/2024");
    }

    TransferDto getTransferDtoWithoutDisplayAmount() {
        TransferDto transferDto = new TransferDto();
        transferDto.setAmount(50.00);
        transferDto.setBuddyUsername("buddy@test");
        transferDto.setDescription("Test");
        transferDto.setDate("23/01/2024");
        return transferDto;
    }

    TransferDto getTransferDtoWithDisplayAmount() {
        TransferDto transferDto = new TransferDto();
        transferDto.setAmount(50.00);
        transferDto.setBuddyUsername("buddy@test");
        transferDto.setDescription("Test");
        transferDto.setDate("23/01/2024");
        transferDto.setDisplayAmount("-50€");
        return transferDto;
    }

    List<TransferDto> getShortListTransfersDto() {
        List<TransferDto> transfersDto = new ArrayList<>();

        TransferDto transferDto1 = getTransferDtoWithDisplayAmount();
        transfersDto.add(transferDto1);

        TransferDto transferDto2 = new TransferDto();
        transferDto2.setAmount(50.00);
        transferDto2.setBuddyUsername("buddy@test");
        transferDto2.setDescription("Test2");
        transferDto2.setDate("23/01/2024");
        transferDto2.setDisplayAmount("+25€");
        transfersDto.add(transferDto2);

        return transfersDto;
    }

    List<TransferDto> getLongListTransfersDto() {
        List<TransferDto> transfersDto = new ArrayList<>();

        TransferDto transferDto1 = getTransferDtoWithDisplayAmount();
        transfersDto.add(transferDto1);
        TransferDto transferDto2 = getTransferDtoWithDisplayAmount();
        transfersDto.add(transferDto2);
        TransferDto transferDto3 = getTransferDtoWithDisplayAmount();
        transfersDto.add(transferDto3);
        TransferDto transferDto4 = getTransferDtoWithDisplayAmount();
        transfersDto.add(transferDto4);
        TransferDto transferDto5 = getTransferDtoWithDisplayAmount();
        transfersDto.add(transferDto5);
        TransferDto transferDto6 = getTransferDtoWithDisplayAmount();
        transfersDto.add(transferDto6);
        TransferDto transferDto7 = getTransferDtoWithDisplayAmount();
        transfersDto.add(transferDto7);
        TransferDto transferDto8 = getTransferDtoWithDisplayAmount();
        transfersDto.add(transferDto8);
        TransferDto transferDto9 = getTransferDtoWithDisplayAmount();
        transfersDto.add(transferDto9);
        TransferDto transferDto10 = getTransferDtoWithDisplayAmount();
        transfersDto.add(transferDto10);
        TransferDto transferDto11 = getTransferDtoWithDisplayAmount();
        transfersDto.add(transferDto11);

        return transfersDto;
    }

    @Test
    public void convertInternalTransferDtoToTransferTest() {
        String currentUsername = "current@user";

        Mockito.when(userService.getUserByEmail(currentUsername)).thenReturn(senderUser);
        Mockito.when(userService.getUserByEmail(transferDto.getBuddyUsername())).thenReturn(receivingUser);

        Transfer transfer = transferMapper.convertTransferDtoToTransfer(transferDto, currentUsername);

        Assertions.assertThat(transfer.getAmount()).isEqualTo(transferDto.getAmount());
        Assertions.assertThat(transfer.getSenderAccount()).isEqualTo(senderAccount);
        Assertions.assertThat(transfer.getRecipientAccount()).isEqualTo(recipientAccount);
        Assertions.assertThat(transfer.getDescription()).isEqualTo(transferDto.getDescription());
        Assertions.assertThat(transfer.getDate()).isEqualTo(transferDto.getDate());
    }

    @Test
    public void convertTransferToTransferDtoIsSentByCurrentUserTest() {

        Mockito.when(userService.getUserByBankAccount(transfer.getRecipientAccount())).thenReturn(receivingUser);

        TransferDto transferDto = transferMapper.convertTransferToTransferDto(transfer, true);

        Assertions.assertThat(transferDto.getAmount()).isEqualTo(transfer.getAmount());
        Assertions.assertThat(transferDto.getDisplayAmount()).isEqualTo("-" + transfer.getAmount() + "€");
        Assertions.assertThat(transferDto.getDescription()).isEqualTo(transfer.getDescription());
        Assertions.assertThat(transferDto.getBuddyUsername()).isEqualTo(receivingUser.getEmail());
        Assertions.assertThat(transferDto.getDate()).isEqualTo(transfer.getDate());
    }

    @Test
    public void convertTransferToTransferDtoIsNotSentByCurrentUserTest() {

        Mockito.when(userService.getUserByBankAccount(transfer.getSenderAccount())).thenReturn(senderUser);

        TransferDto transferDto = transferMapper.convertTransferToTransferDto(transfer, false);

        Assertions.assertThat(transferDto.getAmount()).isEqualTo(transfer.getAmount());
        Assertions.assertThat(transferDto.getDisplayAmount()).isEqualTo("+" + transfer.getAmount() + "€");
        Assertions.assertThat(transferDto.getDescription()).isEqualTo(transfer.getDescription());
        Assertions.assertThat(transferDto.getBuddyUsername()).isEqualTo(senderUser.getEmail());
        Assertions.assertThat(transferDto.getDate()).isEqualTo(transfer.getDate());
    }

    @Test
    public void convertListTransferDtoToPageOfTransferDtoWithShortListTest() {
        List<TransferDto> transfersDto = getShortListTransfersDto();
        PageRequest pageable = PageRequest.of(1 - 1, 5);

        Page<TransferDto> transferDtoPage =
                transferMapper.convertListTransferDtoToPageOfTransferDto(pageable, transfersDto);

        Assertions.assertThat(transferDtoPage).isNotNull();
        Assertions.assertThat(transferDtoPage.getTotalElements()).isEqualTo(transfersDto.size());
        Assertions.assertThat(transferDtoPage.getContent()).containsExactlyElementsOf(transfersDto);
    }

    @Test
    public void convertListTransferDtoToPageOfTransferDtoWhitLongListTest() {
        List<TransferDto> transfersDto = getLongListTransfersDto();
        PageRequest pageable = PageRequest.of(2 - 1, 5);

        Page<TransferDto> transferDtoPage =
                transferMapper.convertListTransferDtoToPageOfTransferDto(pageable, transfersDto);

        Assertions.assertThat(transferDtoPage).isNotNull();
        Assertions.assertThat(transferDtoPage.getTotalElements()).isEqualTo(transfersDto.size());
        List<TransferDto> expectedPageContent = transfersDto.subList(5, 10);
        Assertions.assertThat(transferDtoPage.getContent()).containsExactlyElementsOf(expectedPageContent);
    }
}