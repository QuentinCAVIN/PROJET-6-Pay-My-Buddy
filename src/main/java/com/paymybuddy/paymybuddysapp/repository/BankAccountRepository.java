package com.paymybuddy.paymybuddysapp.repository;

import com.paymybuddy.paymybuddysapp.model.BankAccount;
import com.paymybuddy.paymybuddysapp.model.PersonalBankAccount;
import com.paymybuddy.paymybuddysapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount,Integer> {
    PersonalBankAccount findByIban(String iban);
    // JPA permet visiblement de rechercher par un attribut des classes filles
}
