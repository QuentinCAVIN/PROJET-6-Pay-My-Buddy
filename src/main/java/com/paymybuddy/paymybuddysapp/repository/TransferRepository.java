package com.paymybuddy.paymybuddysapp.repository;

import com.paymybuddy.paymybuddysapp.model.BankAccount;
import com.paymybuddy.paymybuddysapp.model.Transfer;
import com.paymybuddy.paymybuddysapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends JpaRepository<Transfer,Integer> {
    Transfer findBySenderAccount(BankAccount senderAccount);
    Transfer findByRecipientAccount(BankAccount recipientAccount);
    // TODO. Verifier Jpa accepte ces mots-clé
}
