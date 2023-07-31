package com.paymybuddy.paymybuddysapp.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "bank_account")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String iban;

    private String name;

    @Column(name = "account_balance")
    private double accountBalance;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "bank_id")
    private int bankId;
}
