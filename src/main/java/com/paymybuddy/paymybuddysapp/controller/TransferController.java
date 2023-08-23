package com.paymybuddy.paymybuddysapp.controller;

import com.paymybuddy.paymybuddysapp.dto.UserDto;
import com.paymybuddy.paymybuddysapp.model.*;
import com.paymybuddy.paymybuddysapp.service.BankAccountService;
import com.paymybuddy.paymybuddysapp.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TransferController {

    UserService userService;

    BankAccountService bankAccountService;

    public TransferController(UserService userService, BankAccountService bankAccountService) {
        this.userService = userService;
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/transfer")
    public String showTransferPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        UserDto currentUser = userService.getUserDtoByEmail(userDetails.getUsername());
        List<User> buddies = currentUser.getUsersConnexions();

        UserDto buddy = new UserDto();

        TransferDto transfer = new TransferDto();

        model.addAttribute("buddies", buddies); // Pour afficher les buddies de l'utilisateur en cours
        model.addAttribute("buddy", buddy); // Pour le champ nécessaire à l'ajout d'un nouveau buddy
        model.addAttribute("transfer", transfer);//

        return "transfer";
    }



    @PostMapping("/transfer/addBuddy")
    public String addBuddy(@ModelAttribute("buddy") UserDto buddy,
                           @AuthenticationPrincipal UserDetails userDetails,
                           //@ModelAttribute permet à Spring de récupérer les données saisies dans un formulaire
                           //correctement annoté (<form method="post" th:action="@{/transfer/addBuddy}" th:object="${buddy}">).
                           //Et donc construire un objet user avec.

                           BindingResult result,
                           //Sert à collecter les erreurs choisis plus bas et à les gérer.

                           Model model) {

        String emailOfNewBuddy = buddy.getEmail();

        User currentUser = userService.getUserByEmail(userDetails.getUsername());
        // TODO: Modifier le User en UserDto

        User buddyToAdd = userService.getUserByEmail(buddy.getEmail());

        if (emailOfNewBuddy == null || emailOfNewBuddy.isBlank()) {
            result.rejectValue("email", null,
                    "Please fill in your buddy's email");

        } else if (buddyToAdd == null) {
            result.rejectValue("email", null,
                    "There is no account associated to " + emailOfNewBuddy);

        } else if (emailOfNewBuddy.equals(currentUser.getEmail()) ) {
            result.rejectValue("email", null,
                    "Really? it's too sad... go get some friends");

        } else {

            //Check that new buddy is not already added
            currentUser.getUsersConnexions().forEach(user -> {
                if (user.getEmail().equals(buddyToAdd.getEmail())) {
                    result.rejectValue("email", null,
                            buddyToAdd.getFirstName() + " " + buddyToAdd.getLastName() +
                                    " is already add to your buddies!");
                }
            });
        }

        if (result.hasErrors()) {

            model.addAttribute("buddy", buddy);

            //TODO : vérifier si il n'y a pas un meilleur moyen que de recopier la methode /transfer
            // Pour recharger la meme page avec un message d'erreur approprié
            List<User> buddies = currentUser.getUsersConnexions();
            model.addAttribute("buddies", buddies);

            TransferDto transfer = new TransferDto();
            model.addAttribute("transfer", transfer);//

            //

            return "/transfer";
        }

        currentUser.addConnexion(buddyToAdd); //TODO passer par une classe Service pour faire ça?
        userService.saveUser(currentUser);


        return "redirect:/transfer?success";
        // TODO : rajouter un message de confirmation attention a ne pas générer d'autre message de succès inappropriées
        //On recharge la page
    }


    @GetMapping("/transfer/deleteBuddy")
    public String deleteBuddy(@RequestParam("email") final String buddysEmailToRemove,
                              @AuthenticationPrincipal UserDetails userDetails) {

        User currentUser = userService.getUserByEmail(userDetails.getUsername());
        User buddyToRemove = userService.getUserByEmail(buddysEmailToRemove);

        currentUser.removeConnexion(buddyToRemove);
        userService.saveUser(currentUser);

        return "redirect:/transfer";
    }

    // TODO: Ci-dessous pas de test effectué.
    // TODO: METHODE EN COURS DE DEVELOPPEMENT, Gerer les valeur negatives et autre exceptions + ajout de la possibilité
    //  de faire des virement sur son compte perso + reflechir si placer les virement perso au même endroit dans le html
    @PostMapping("/transfer/sendMoney")
    public String sendMoney(@ModelAttribute("transfer") TransferDto transferDto,
                            @AuthenticationPrincipal UserDetails userDetails,
                            Model model, BindingResult result ) {

        UserDto currentUser = userService.getUserDtoByEmail(userDetails.getUsername());
        UserDto userSelected = userService.getUserDtoByEmail(transferDto.getUsernamePayMyBuddyRecipientAccount());

        BankAccount senderAccount = currentUser.getPayMyBuddyBankAccount();
        BankAccount recipientAccount = userSelected.getPayMyBuddyBankAccount();
        double transferAmount = transferDto.getAmount();

        if (transferAmount <= 0){
            result.rejectValue("amount", null,
                    "Incorrect amount value");
        }

        else if (senderAccount.getAccountBalance() <= transferAmount){
            result.rejectValue("amount", null,
                    "you do not have enough money in your account");

        }

        if (result.hasErrors()) {

            //TODO : vérifier si il n'y a pas un meilleur moyen que de recopier la methode /transfer
            // Pour recharger la meme page avec un message d'erreur approprié

            UserDto buddy = new UserDto();
            model.addAttribute("buddy", buddy);

            List<User> buddies = currentUser.getUsersConnexions();
            model.addAttribute("buddies", buddies);

            TransferDto transfer = new TransferDto();
            model.addAttribute("transfer", transfer);//

            return "/transfer";
        }

            Transfer transfer = new Transfer();
            transfer.setAmount(transferDto.getAmount());
            transfer.setSenderAccount(senderAccount);
            transfer.setRecipientAccount(recipientAccount);

            bankAccountService.transfer(transfer);
            return "redirect:/transfer?success";
            // TODO : rajouter un message de confirmation attention a ne pas générer d'autre message de succès inappropriées
    }
}
