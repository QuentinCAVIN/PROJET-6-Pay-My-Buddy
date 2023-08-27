package com.paymybuddy.paymybuddysapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name ="paymybuddy_bank_account" )
public class PayMyBuddyBankAccount extends BankAccount{

    @OneToOne(
            cascade = CascadeType.ALL, //Supprimer un utilisateur supprimera son compte associé
            orphanRemoval = true, //garantit la non existance de compte orphelin
            fetch = FetchType.EAGER // Quand on récupére un User on récupére le compte associé
    )
    @JoinColumn(name ="personal_bank_account_id" )
    private PersonalBankAccount personalBankAccount;
    //TODO Reflexion faite , il serait plus logique d'associer PersonalBankAccount a User
}

