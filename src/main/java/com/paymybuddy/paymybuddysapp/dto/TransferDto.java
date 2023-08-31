package com.paymybuddy.paymybuddysapp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

    @Getter
    @Setter
    @NoArgsConstructor
    public class TransferDto {
        double amount;
        String buddyUsername;
        String description;
        String date;
        String displayAmount;
    }

