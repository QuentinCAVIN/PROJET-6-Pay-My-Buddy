package com.paymybuddy.paymybuddysapp.repository;

import com.paymybuddy.paymybuddysapp.model.PayMyBuddyBankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayMyBuddyBankAccountRepository extends JpaRepository<PayMyBuddyBankAccount, Integer> {
}
