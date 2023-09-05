package com.paymybuddy.paymybuddysapp.controller;

import com.paymybuddy.paymybuddysapp.dto.BankAccountDto;
import com.paymybuddy.paymybuddysapp.dto.TransferDto;
import com.paymybuddy.paymybuddysapp.dto.UserDto;
import com.paymybuddy.paymybuddysapp.mapper.BankAccountMapper;
import com.paymybuddy.paymybuddysapp.mapper.UserMapper;
import com.paymybuddy.paymybuddysapp.model.*;
import com.paymybuddy.paymybuddysapp.repository.PersonalBankAccountRepository;
import com.paymybuddy.paymybuddysapp.service.BankAccountService;
import com.paymybuddy.paymybuddysapp.service.TransferService;
import com.paymybuddy.paymybuddysapp.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    UserService userService;

    @Autowired
    BankAccountService bankAccountService;

    //TODO Classe à tester: tests unitaires + intégration

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal UserDetails userDetails, Model model) { // Spring va fournir une instance de cet objet Model (keske C?)

        User currentUser = userService.getUserByEmail(userDetails.getUsername());

        List<User> buddies = currentUser.getUsersConnexions();
        List<UserDto> buddiesDto = UserMapper.convertUserListToUserDtoList(buddies);
        model.addAttribute("buddies", buddiesDto);
        //addAttibute va permettre d'ajouter
        // au model, un objet. Le premier paramètre c'est le nom de l'objet sur la page html
        // ,le second c'est l'objet.
        //c'est grace à ça, et avec Thymeleaf, qu'on va pouvoir utiliser les objets dans le html
        //en utilisant les noms de l'objet ${users}


        //Pour ajouter un nouveau compte perso
        BankAccountDto personalBankAccountToFile = new BankAccountDto();
        model.addAttribute("personalBankAccountToFile",personalBankAccountToFile);

        // Pour afficher le montant du compte perso
        PersonalBankAccount currentUserPersonalBankAccount = currentUser.getPersonalBankAccount();
        BankAccountDto personalBankAccountToDisplay =
                BankAccountMapper.convertBankAccountToBankAccountDto(currentUserPersonalBankAccount);
        model.addAttribute("personalBankAccountToDisplay", personalBankAccountToDisplay);

        // pour afficher le montant du compte pay my buddy
        PayMyBuddyBankAccount payMyBuddyBankAccount = currentUser.getPayMyBuddyBankAccount();
        BankAccountDto currentUserPayMyBuddyAccount =
                BankAccountMapper.convertBankAccountToBankAccountDto(payMyBuddyBankAccount);
        model.addAttribute("payMyBuddyBankAccount",payMyBuddyBankAccount);

        //Echanger argent entre les deux comptes
        TransferDto transferDto = new TransferDto();
        model.addAttribute("transfer",transferDto);

        //////Temporaire Pour faciliter les tests manuel, ne pas tester
        List<User> users = userService.getUsers();
        List<UserDto> usersDto = UserMapper.convertUserListToUserDtoList(users);
        model.addAttribute("users", usersDto);
        //////

        return "home";
    }

    @Transactional
    @PostMapping("/home/linkPersonalBankAccount")
    public String linkPersonalBankAccount(@AuthenticationPrincipal UserDetails userDetails,
                                          @ModelAttribute("personalBankAccount") BankAccountDto personalBankAccountDto,
                                          Model model){
        User curentUser= userService.getUserByEmail(userDetails.getUsername());

        PersonalBankAccount personalBankAccount =
                BankAccountMapper.convertBankAccountDtoToPersonalBankAccount(personalBankAccountDto);

        bankAccountService.linkNewPersonalBankAccount(personalBankAccount,curentUser);

        return "redirect:/home?success";
    }

    @Transactional
    @PostMapping("/home/creditAndCashIn")
    public String creditAndCashIn(@RequestParam String button, @ModelAttribute TransferDto transferDto,
                                  BindingResult result) {
        //Le paramètre button est ajouté dans le html et va permettre d'utiliser ces conditions :
        Transfer transfer = new Transfer();

        if (button.equals("credit")) {
            //TODO: construire un objet Transfer différent en fonction du boutton
        } else if ("cashIn".equals(button)) {
            System.out.println("CASHHHHIN");
        }
        bankAccountService.transfer(transfer);
        return "redirect:/home?success"; // Redirigez vers la page appropriée après traitement.
    }


}
