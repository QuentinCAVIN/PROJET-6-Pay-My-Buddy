package com.paymybuddy.paymybuddysapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "paymybuddy_bank_account")
public class PayMyBuddyBankAccount extends BankAccount {
}