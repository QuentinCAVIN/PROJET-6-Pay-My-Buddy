package com.paymybuddy.paymybuddysapp.repository;

import com.paymybuddy.paymybuddysapp.model.BankAccount;
import com.paymybuddy.paymybuddysapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
  User findByEmail(String email);
}

