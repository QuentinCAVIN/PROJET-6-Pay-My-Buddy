package com.paymybuddy.paymybuddysapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
public abstract class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "account_balance", nullable = false)
    private double accountBalance;

    @OneToMany(
            mappedBy = "senderAccount",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true
    )
    private List<Transfer> sentTransfers = new ArrayList<>();

    @OneToMany(
            mappedBy = "recipientAccount",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true
    )
    private List<Transfer> receivedTransfers = new ArrayList<>();

    public void addSentTransfer(Transfer transfer) {
        //Si oui, les supprimer
        sentTransfers.add(transfer);
        transfer.setSenderAccount(this);
    }

    public void removeConnexion(Transfer transfer) {
        sentTransfers.remove(transfer);
        transfer.setSenderAccount(null);
    }

    public void addReceivedTransfer(Transfer transfer) {
        receivedTransfers.add(transfer);
        transfer.setRecipientAccount(this);
    }

    public void removeReceivedTransfer(Transfer transfer) {
        receivedTransfers.remove(transfer);
        transfer.setRecipientAccount(null);
    }
}