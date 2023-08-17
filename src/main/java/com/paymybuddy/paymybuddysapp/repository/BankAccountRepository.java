package com.paymybuddy.paymybuddysapp.repository;

import com.paymybuddy.paymybuddysapp.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {
}
