package com.paymybuddy.paymybuddysapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass //La classe parente ne sera pas persisté en BDD
@Getter
@Setter
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "account_balance" ,nullable=false)
    private double accountBalance;
}
 /*https://www.baeldung.com/hibernate-inheritance*/