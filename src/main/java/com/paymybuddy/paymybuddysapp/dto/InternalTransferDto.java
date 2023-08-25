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
    }

