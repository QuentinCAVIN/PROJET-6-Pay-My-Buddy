package com.paymybuddy.paymybuddysapp.controller;

import com.paymybuddy.paymybuddysapp.dto.UserDto;
import com.paymybuddy.paymybuddysapp.model.User;
import com.paymybuddy.paymybuddysapp.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class TransferController {

    UserService userService;

    public TransferController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/transfer")
    public String showTransferPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        UserDto currentUser = userService.getUserDtoByEmail(userDetails.getUsername());
        List<User> buddies = currentUser.getUsersConnexions();

        UserDto buddy = new UserDto();

        model.addAttribute("buddies", buddies);
        model.addAttribute("buddy", buddy);

        return "transfer";
    }


    //TODO: la methode permet d'ajouter un utilisateur a lui même, corriger ou adapter ça.
    @PostMapping("/transfer/addBuddy")
    public String addBuddy(@ModelAttribute("buddy") UserDto buddy,
                           @AuthenticationPrincipal UserDetails userDetails,
                           //@ModelAttribute permet à Spring de récupérer les données saisies dans un formulaire
                           //correctement annoté (<form method="post" th:action="@{/saveUser}" th:object="${user}">).
                           //Et donc construire un objet user avec.

                           BindingResult result,
                           //Sert à collecter et gérer les résultats des @Valid

                           Model model) {

        String emailOfNewBuddy = buddy.getEmail();

        User currentUser = userService.getUserByEmail(userDetails.getUsername());
        // TODO: Ici je pourrai remplacer par UserDto, mais je ne vosi pas ce que ça apporte.

        User buddyToAdd = userService.getUserByEmail(buddy.getEmail());


        if (buddyToAdd == null) {

            result.rejectValue("email", null,
                    "There is no account associated to " + emailOfNewBuddy);

            // On recharge la page en cas d'erreur
            //Spring ajoute automatiquement l'objet BindingResult au model
            //la nouvelle page prendra les erreurs en compte(th:errors).

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
            //

            return "/transfer";
        }

        currentUser.addConnexion(buddyToAdd); //TODO passer par une classe Service pour faire ça?
        userService.saveUser(currentUser);


        return "redirect:/transfer?success"; // TODO : rajouter un message de confirmation
        //On recharge la page
    }


    @GetMapping("/transfer/deleteBuddy")
    public String deleteConnection(@RequestParam("email") final String buddysEmailToRemove, //TODO Remplacer par RequestParam email?
                                   @AuthenticationPrincipal UserDetails userDetails) {

        User currentUser = userService.getUserByEmail(userDetails.getUsername());
        User buddyToRemove = userService.getUserByEmail(buddysEmailToRemove);

        currentUser.removeConnexion(buddyToRemove);
        userService.saveUser(currentUser);

        return "redirect:/transfer";
    }

}
