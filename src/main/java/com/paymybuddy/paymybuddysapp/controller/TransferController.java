package com.paymybuddy.paymybuddysapp.controller;

import com.paymybuddy.paymybuddysapp.dto.UserDto;
import com.paymybuddy.paymybuddysapp.model.User;
import com.paymybuddy.paymybuddysapp.service.UserService;
import jakarta.validation.Valid;
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

        model.addAttribute("buddies", buddies); // Pour afficher les buddies de l'utilisateur en cours
        model.addAttribute("buddy", buddy); // Pour le champ nécessaire à l'ajout d'un nouveau buddy

        return "transfer";
    }


    //TODO: la methode permet d'ajouter un utilisateur a lui même, corriger ou adapter ça.
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
        // TODO: Ici je pourrai remplacer par UserDto, mais je ne vois pas ce que ça apporte.

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
            //

            return "/transfer";
        }

        currentUser.addConnexion(buddyToAdd); //TODO passer par une classe Service pour faire ça?
        userService.saveUser(currentUser);


        return "redirect:/transfer?success"; // TODO : rajouter un message de confirmation
        //On recharge la page
    }


    @GetMapping("/transfer/deleteBuddy")
    public String deleteConnection(@RequestParam("email") final String buddysEmailToRemove,
                                   @AuthenticationPrincipal UserDetails userDetails) {

        User currentUser = userService.getUserByEmail(userDetails.getUsername());
        User buddyToRemove = userService.getUserByEmail(buddysEmailToRemove);

        currentUser.removeConnexion(buddyToRemove);
        userService.saveUser(currentUser);

        return "redirect:/transfer";
    }
}
