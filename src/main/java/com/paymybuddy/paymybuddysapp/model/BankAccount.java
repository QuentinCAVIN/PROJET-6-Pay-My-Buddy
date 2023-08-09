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

    @Column(nullable=false)
    private String iban;

    @Column(nullable=false)
    private String name;

    @Column(name = "account_balance" ,nullable=false)
    private double accountBalance;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "bank_id")
    private int bankId;
}
