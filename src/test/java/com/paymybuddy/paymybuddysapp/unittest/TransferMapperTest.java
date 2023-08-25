package com.paymybuddy.paymybuddysapp.unittest;

import com.paymybuddy.paymybuddysapp.dto.InternalTransferDto;
import com.paymybuddy.paymybuddysapp.mapper.TransferMapper;
import com.paymybuddy.paymybuddysapp.model.PayMyBuddyBankAccount;
import com.paymybuddy.paymybuddysapp.model.Transfer;
import com.paymybuddy.paymybuddysapp.model.User;
import com.paymybuddy.paymybuddysapp.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.parameters.P;

@ExtendWith(MockitoExtension.class)
public class TransferMapperTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private TransferMapper transferMapper;
    private static User senderUser;
    private static User receivingUser;
    private static InternalTransferDto transferDto;
    private static PayMyBuddyBankAccount senderAccount;
    private static PayMyBuddyBankAccount recipientAccount;

    @BeforeAll
    public static void setup(){

        senderAccount = new PayMyBuddyBankAccount();
        senderAccount.setAccountBalance(500);
        recipientAccount = new PayMyBuddyBankAccount();
        recipientAccount.setAccountBalance(500);

        senderUser  = new User();
        receivingUser = new User();
        senderUser.setPayMyBuddyBankAccount(senderAccount);
        receivingUser.setPayMyBuddyBankAccount(recipientAccount);

        transferDto = new InternalTransferDto();
        transferDto.setAmount(50.00);
        transferDto.setUsernameOfSenderAccount("senderUser@test");
        transferDto.setUsernameOfRecipientAccount("receivingUser@test");
        transferDto.setDescription("Test");
        transferDto.setDate("23/01/2024");
    }

    @Test
    public void convertInternalTrasferDtoToTransferTest(){

        Mockito.when(userService.getUserByEmail(transferDto.getUsernameOfSenderAccount())).thenReturn(senderUser);
        Mockito.when(userService.getUserByEmail(transferDto.getUsernameOfRecipientAccount())).thenReturn(receivingUser);

        Transfer transfer = transferMapper.convertInternalTransferDtoToTransfer(transferDto);

        Assertions.assertThat(transfer.getAmount()).isEqualTo(transferDto.getAmount());
        Assertions.assertThat(transfer.getSenderAccount()).isEqualTo(senderAccount);
        Assertions.assertThat(transfer.getRecipientAccount()).isEqualTo(recipientAccount);
        Assertions.assertThat(transfer.getDescription()).isEqualTo(transferDto.getDescription());
        Assertions.assertThat(transfer.getDate()).isEqualTo(transferDto.getDate());
    }
}
