package com.paymybuddy.paymybuddysapp;

import com.paymybuddy.paymybuddysapp.model.BankAccount;
import com.paymybuddy.paymybuddysapp.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PaymybuddysappApplication implements CommandLineRunner {
	@Autowired
	private BankAccountService bankAccountService;

	public static void main(String[] args) {
		SpringApplication.run(PaymybuddysappApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception{
		Iterable<BankAccount> bankAccounts = bankAccountService.getBankAccounts();
		bankAccounts.forEach(bankAccount -> System.out.println(bankAccount.getName()));
	}

}
