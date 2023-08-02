package com.paymybuddy.paymybuddysapp;

import com.paymybuddy.paymybuddysapp.model.BankAccount;
import com.paymybuddy.paymybuddysapp.model.User;
import com.paymybuddy.paymybuddysapp.service.BankAccountService;
import com.paymybuddy.paymybuddysapp.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;

@SpringBootApplication
public class PaymybuddysappApplication implements CommandLineRunner {
	@Autowired
	private BankAccountService bankAccountService;

	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(PaymybuddysappApplication.class, args);
	}

	@Override
	@Transactional // obligatoire pour gérer le "proxy"?? quand on utilise FetchType.LAZY sur la relation
	public void run(String... args) throws Exception{
		/*Iterable<BankAccount> bankAccounts = bankAccountService.getBankAccounts();
		bankAccounts.forEach(bankAccount -> System.out.println("Iban n°" + bankAccount.getIban()));
		Iterable<User> users = userService.getUsers();
		users.forEach(a -> System.out.println(a.getFirstName() + " " + a.getLastName()));*/

		Optional<User> optionalUser = userService.getUserById(2);
		User user2 = optionalUser.get();
		user2.getUsersConnexions().forEach(user -> System.out.println(user.getFirstName()));

	}

}
