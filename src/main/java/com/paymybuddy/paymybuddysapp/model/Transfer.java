package com.paymybuddy.paymybuddysapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class Transfer {

    private int id;
    private double amount;
    private BankAccount senderAccount;
    private BankAccount recipientAccount;
    private String description;
    private String date;
}
