package com.paymybuddy.paymybuddysapp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

    @Getter
    @Setter
    @NoArgsConstructor
    public class TransferDto {
        double amount;
        String usernamePayMyBuddyRecipientAccount; // pour récupérer le compte d'un Buddy
        String usernamePersonalRecipientAccount; //Pour récupérer le compte lié a l'utilisateur
    }

