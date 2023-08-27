package com.paymybuddy.paymybuddysapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "transfer")
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable=false)
    private double amount;

    @ManyToOne(
            cascade = CascadeType.ALL
    )
    @JoinColumn(name="sender_account_id")

    private BankAccount senderAccount;

    @JoinColumn(name="recipient_account_id", nullable=false)
    @ManyToOne
    private BankAccount recipientAccount;

    @Column(nullable=false)
    private String description;

    @Column(nullable=false)
    private String date;
}