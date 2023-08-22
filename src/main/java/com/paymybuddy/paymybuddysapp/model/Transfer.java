package com.paymybuddy.paymybuddysapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Transfer {
    double amount;
    BankAccount senderAccount;
    BankAccount recipientAccount;
}
