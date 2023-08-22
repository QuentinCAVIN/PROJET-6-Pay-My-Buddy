package com.paymybuddy.paymybuddysapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name ="personal_bank_account" )
public class PersonalBankAccount extends BankAccount {


    @Column(nullable=false)
    private String iban;


}
