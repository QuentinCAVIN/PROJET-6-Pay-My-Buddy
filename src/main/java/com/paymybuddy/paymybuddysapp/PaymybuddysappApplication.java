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

		lancerLeTestDeLaFonctionnalite(1, "snoobydoo@hotmail.com");
	}

	//TODO: Construire correctement la méthode et la placer au bonne endroit.
	// Placé ici pour réaliser la fonctionnalité demandée par Vincent avant de structurer les différentes couches
	public void addConnexion(int currentUserId, /* remplacer par User CurrentUser avec @AuthenticationPrincipal */
			String emailUserToAdd) {

		Optional<User> optionalCurrentUser = userService.getUserById(currentUserId);
		User currentUser = optionalCurrentUser.get();

		Optional<User> optionalUserToAdd = userService.getUserByEmail(emailUserToAdd);
		User userToAdd = optionalUserToAdd.get();

		System.out.println("\nL'utilisateur "+ currentUser.getFirstName() + " " + userToAdd.getLastName()
				+ " ajoute l'addresse mail " + emailUserToAdd +" correspondant à l'utilisateur "
				+ userToAdd.getFirstName() + userToAdd.getLastName() +".");

		currentUser.addConnexion(userToAdd);
	}

	public void removeConnexion(int currentUserId, String emailUserToRemove) {

		Optional<User> optionalCurrentUser = userService.getUserById(currentUserId);
		User currentUser = optionalCurrentUser.get();

		Optional<User> optionalUserToRemove = userService.getUserByEmail(emailUserToRemove);
		User userToRemove = optionalUserToRemove.get();

		System.out.println("\nL'utilisateur "+ currentUser.getFirstName() + " " + userToRemove.getLastName()
				+ " supprime l'addresse mail " + emailUserToRemove +" correspondant à l'utilisateur "
				+ userToRemove.getFirstName() + userToRemove.getLastName() +".");

		currentUser.removeConnexion(userToRemove);
	}

	public void afficherLesConnexionsDeLUtilisateur (int idUser) {
		Optional<User> optionalUser = userService.getUserById(idUser);
		User user = optionalUser.get();

		System.out.println("\nUtilisateurs ajouté par " + user.getFirstName() + ":\n");
		user.getUsersConnexions().forEach(userConnexion-> System.out.println(userConnexion.getFirstName()));

		System.out.println("\nUtilisateurs qui ont ajouté " + user.getFirstName() + ":\n");
		user.getUsersConnected().forEach(userConnected -> System.out.println(userConnected.getFirstName()));
	}
	public void afficherBuddyDeLUtilisateur (int idUser) {

		Optional<User> optionalUser = userService.getUserById(idUser);
		User user = optionalUser.get();

		System.out.println("\nUtilisateurs ajouté par " + user.getFirstName() + " " + user.getLastName() + ":");
		user.getUsersConnexions().forEach(userConnexion-> System.out.println(userConnexion.getFirstName()));
	}


	public void afficherBuddyDeLUtilisateur (String emailUser) {

		Optional<User> optionalUser = userService.getUserByEmail(emailUser);
		User user = optionalUser.get();

		afficherBuddyDeLUtilisateur(user.getId());
	}

	public void afficherQuiAAjouteLutilisateurCommeBuddy(String emailUser) {
		Optional<User> optionalUser = userService.getUserByEmail(emailUser);
		User user = optionalUser.get();

		System.out.println("\nUtilisateurs qui ont ajouté " + user.getFirstName() + " " + user.getLastName() + ":");
		user.getUsersConnected().forEach(userConnected-> System.out.println(userConnected.getFirstName()));
	}


	public void lancerLeTestDeLaFonctionnalite (int idCurrentUser, String emailUserToAddAndDelete){

		System.out.println("\nLancement du Test." );

		System.out.println("\nEtat des connexions utilisateurs avant le lancement de la methode:");
		afficherBuddyDeLUtilisateur(idCurrentUser);
		afficherQuiAAjouteLutilisateurCommeBuddy(emailUserToAddAndDelete);

		addConnexion(idCurrentUser,emailUserToAddAndDelete);

		System.out.println("\nEtat des connexions utilisateurs après le lancement de la methode:");
		afficherBuddyDeLUtilisateur(idCurrentUser);
		afficherQuiAAjouteLutilisateurCommeBuddy(emailUserToAddAndDelete);

		removeConnexion(idCurrentUser,emailUserToAddAndDelete);

		System.out.println("\nRetour à l'état initial.");
		afficherBuddyDeLUtilisateur(idCurrentUser);
		afficherQuiAAjouteLutilisateurCommeBuddy(emailUserToAddAndDelete);

	}

}
