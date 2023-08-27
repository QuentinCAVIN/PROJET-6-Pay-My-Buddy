package com.paymybuddy.paymybuddysapp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

    @Getter
    @Setter
    @NoArgsConstructor
    public class InternalTransferDto {
        double amount;
        String usernameOfSenderAccount; //Pour récupérer le compte lié a l'utilisateur
        String usernameOfRecipientAccount; // pour récupérer le compte d'un Buddy
        String description;
        String date;

        //TODO: On peut remplacer String usernameOfSenderAccount par l'ID du BankAccount
        // Cela permetrait de supprimmer InternalTransferDto et ExternalTransferDto par une seul classe TransferDto
        // vérifier que les id sont bien différentes entre PayMyBuddyBankAccount et PersonalBankAccount
    }

