package com.paymybuddy.paymybuddysapp.controller;

import com.paymybuddy.paymybuddysapp.dto.TransferDto;
import com.paymybuddy.paymybuddysapp.dto.UserDto;
import com.paymybuddy.paymybuddysapp.mapper.TransferMapper;
import com.paymybuddy.paymybuddysapp.mapper.UserMapper;
import com.paymybuddy.paymybuddysapp.model.*;
import com.paymybuddy.paymybuddysapp.service.BankAccountService;
import com.paymybuddy.paymybuddysapp.service.TransferService;
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

    TransferService transferService;

    TransferMapper transferMapper;


    public TransferController(UserService userService, BankAccountService bankAccountService,
                              TransferMapper transferMapper, TransferService transferService) {
        this.userService = userService;
        this.bankAccountService = bankAccountService;
        this.transferMapper = transferMapper;
        this.transferService = transferService;
    }

    @GetMapping("/transfer")
    public String showTransferPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        User currentUser = userService.getUserByEmail(userDetails.getUsername());

        //For display user's buddies
        List<User> buddies = currentUser.getUsersConnexions();
        List<UserDto> buddiesDto = UserMapper.convertUserListToUserDtoList(buddies);
        model.addAttribute("buddies", buddiesDto);

        //For display user's transfers from his PayMyBuddyAccount
        List<TransferDto> transfersDto = transferService
                .getTransfersDtoByBankAccount(currentUser.getPayMyBuddyBankAccount());
        model.addAttribute( "transfers",transfersDto);

        //For add a new buddy
        UserDto buddy = new UserDto();
        model.addAttribute("buddy", buddy);

        //For add a new transfer
        TransferDto transferDto = new TransferDto();
        model.addAttribute("transfer", transferDto);

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

        } else if (emailOfNewBuddy.equals(currentUser.getEmail())) {
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
            List<UserDto> buddiesDto = UserMapper.convertUserListToUserDtoList(buddies);
            model.addAttribute("buddies", buddiesDto);

            TransferDto transfer = new TransferDto();
            model.addAttribute("transfer", transfer);//

            List<TransferDto> transfersDto = transferService
                    .getTransfersDtoByBankAccount(currentUser.getPayMyBuddyBankAccount());
            model.addAttribute( "transfers",transfersDto);

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
    @Transactional
    @PostMapping("/transfer/sendMoney")
    public String sendMoney(@ModelAttribute("transfer") TransferDto transferDto,
                            @AuthenticationPrincipal UserDetails userDetails,
                            Model model, BindingResult result) {

        User currentUser = userService.getUserByEmail(userDetails.getUsername());
        User userSelected = userService.getUserByEmail(transferDto.getBuddyUsername());

        BankAccount senderAccount = currentUser.getPayMyBuddyBankAccount();
        double transferAmount = transferDto.getAmount();

        if (transferAmount <= 0) {
            result.rejectValue("amount", null,
                    "Incorrect amount value");
        } else if (senderAccount.getAccountBalance() <= transferAmount) {
            result.rejectValue("amount", null,
                    "you do not have enough money in your account");
        }

        if (result.hasErrors()) {

            //TODO : vérifier si il n'y a pas un meilleur moyen que de recopier la methode /transfer
            // Pour recharger la meme page avec un message d'erreur approprié

            UserDto buddy = new UserDto();
            model.addAttribute("buddy", buddy);

            List<User> buddies = currentUser.getUsersConnexions();
            List<UserDto> buddiesDto = UserMapper.convertUserListToUserDtoList(buddies);
            model.addAttribute("buddies", buddiesDto);

            TransferDto transfer = new TransferDto();
            model.addAttribute("transfer", transfer);

            List<TransferDto> transfersDto = transferService
                    .getTransfersDtoByBankAccount(currentUser.getPayMyBuddyBankAccount());
            model.addAttribute( "transfers",transfersDto);

            return "/transfer";
        }
        Transfer transfer = transferMapper.convertTransferDtoToTransfer(transferDto,currentUser.getEmail());
        transferService.createNewTransfer(transfer);
        // TODO : utiliser transfersSERVICE . SAVE DU TRANSFER/ UserNameOfSenderAccount
        //  définis dans /transfer/sendMoney (set juste au dessus)
        //  +UsernameOfSenderAccount definis dans le @ModelAttribute selectionné par l'utilisateur via menu déroulan
        //      +Amount définis ModelAttribute.


        // TODO : STOP HERE!!!! /REPRENDRE LA SAUVEGARDE D'UN TRANSFER EN BDD. IMPLEMENTER TRANSFER SERVICE POUR ça

        bankAccountService.transfer(transfer);
        return "redirect:/transfer?success";
        // TODO : rajouter un message de confirmation attention a ne pas générer d'autre message de succès inappropriées
    }





    /*private void loadTransferPageElements(User currentUser, Model model) {
        List<User> buddies = currentUser.getUsersConnexions();
        List<UserDto> buddiesDto = UserMapper.convertUserListToUserDtoList(buddies);
        model.addAttribute("buddies", buddiesDto);

        List<TransferDto> transfersDto = transferService
                .getTransfersDtoByBankAccount(currentUser.getPayMyBuddyBankAccount());
        model.addAttribute("transfers", transfersDto);

        UserDto buddy = new UserDto();
        model.addAttribute("buddy", buddy);

        TransferDto transferDto = new TransferDto();
        model.addAttribute("transfer", transferDto);
    }*/
    //TODO: pour essayer d'éviter de duplique du code mais ne récupére pas les erreurs. A retravailler ou laisser tomber
    // Regarde pour ajouter le resultat du bindigResul au modele
    /*    if (result.hasErrors()) {
        for (FieldError error : result.getFieldErrors()) {
            model.addAttribute(error.getField() + "Error", error.getDefaultMessage());
        }*/
}