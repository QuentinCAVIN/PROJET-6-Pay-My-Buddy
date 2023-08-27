package com.paymybuddy.paymybuddysapp.repository;

import com.paymybuddy.paymybuddysapp.model.PayMyBuddyBankAccount;
import com.paymybuddy.paymybuddysapp.model.PersonalBankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PersonalBankAccountRepository extends JpaRepository<PersonalBankAccount, Integer> {

    //TODO : Trouver le moyen de faire une recherche par user.email @Query
}