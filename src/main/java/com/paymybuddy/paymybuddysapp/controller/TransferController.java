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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    public String showTransferPage(@AuthenticationPrincipal UserDetails userDetails, Model model,
                                   @RequestParam("page") Optional<Integer> page,
                                   @RequestParam("size") Optional<Integer> size) {
        // Le parametre "size" est inutile ici : @{/transfer(size=${transfers.size}, il sera utile en cas
        // d'implémentation d'une fonctionnalité pour permettre à l'utilisateur de changer le nombre d'objets par page

        User currentUser = userService.getUserByEmail(userDetails.getUsername());

        //////For display user's buddies and transfers
        loadTransferPageElements(currentUser, model, page, size);
        //////

        //////For add a new buddy
        UserDto buddy = new UserDto();
        model.addAttribute("buddy", buddy);
        //////

        //////For add a new transfer
        TransferDto transferDto = new TransferDto();
        model.addAttribute("transfer", transferDto);
        //////

        return "transfer";
    }

    private void loadTransferPageElements(User currentUser, Model model,
                                          Optional<Integer> page, Optional<Integer> size) {

        //////For display user's buddies
        List<User> buddies = currentUser.getUsersConnexions();
        List<UserDto> buddiesDto = UserMapper.convertUserListToUserDtoList(buddies);
        model.addAttribute("buddies", buddiesDto);
        //////

        //////For Pagination
        int currentPage = page.orElse(1);// Définis le numéro de page, =1 si pas spécifié
        int pageSize = size.orElse(5);// Définis le nombre d'éléments par page, toujours 5 ici car jamais spécifié

        //Get transfers associated with the user's BankAccount and converts them into a pagination of transfers
        PayMyBuddyBankAccount accountOfCurrentUser = currentUser.getPayMyBuddyBankAccount();
        List<TransferDto> transfersDtoOfCurrentUser = transferService.getTransfersDtoByBankAccount(accountOfCurrentUser);
        Page<TransferDto> transfersDtoPage = transferMapper.convertListTransferDtoToPageOfTransferDto(PageRequest.of(
                currentPage - 1, pageSize), transfersDtoOfCurrentUser);

        model.addAttribute("transfers", transfersDtoPage);

        //Generation of page numbers for the paging UI
        int totalPages = transfersDtoPage.getTotalPages();// On récupère le nombre total de pages
        if (totalPages > 0) {//On vérifie que ce nombre est supérieur à 0
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)//crée un flux de "int" de 1 à "totalPages" inclus
                    .boxed() // convertit les entiers du flux en objet Integer
                    .collect(Collectors.toList()); // collecte les Integers du flux et les places dans une liste
            model.addAttribute("pageNumbers", pageNumbers);
        }
        //////
    }

    private void loadTransferPageElements(User currentUser, Model model, TransferDto transferDto) {
        model.addAttribute("transfer",transferDto);
        UserDto buddy = new UserDto();
        model.addAttribute("buddy",buddy);
        loadTransferPageElements(currentUser, model, Optional.empty(), Optional.empty());
    }

    private void loadTransferPageElements(User currentUser, Model model, UserDto buddy) {
        model.addAttribute("buddy",buddy);
        TransferDto transferDto = new TransferDto();
        model.addAttribute("transfer",transferDto);
        loadTransferPageElements(currentUser, model, Optional.empty(), Optional.empty());
    }

    private void loadTransferPageElements(User currentUser, Model model) {
        UserDto buddy = new UserDto();
        model.addAttribute("buddy",buddy);
        TransferDto transferDto = new TransferDto();
        model.addAttribute("transfer",transferDto);
        loadTransferPageElements(currentUser, model, Optional.empty(), Optional.empty());
    }


    @PostMapping("/transfer/addBuddy")
    public String addBuddy(@ModelAttribute("buddy") UserDto buddy,
                           @AuthenticationPrincipal UserDetails userDetails,
                           //@ModelAttribute permet à Spring de récupérer les données saisies dans un formulaire
                           //correctement annoté (<form method="post" th:action="@{/transfer/addBuddy}" th:object="${buddy}">).
                           //Et donc construire un objet userDto avec.

                           BindingResult result,
                           //Sert à collecter les erreurs choisis plus bas et à les gérer.

                           Model model) {

        String emailOfNewBuddy = buddy.getEmail();
        User currentUser = userService.getUserByEmail(userDetails.getUsername());
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
            loadTransferPageElements(currentUser, model,buddy);
            return "/transfer";
        }

        currentUser.addConnexion(buddyToAdd); //TODO passer par une classe Service pour faire ça? Voir pour simplement supprimer cette ligne
        userService.saveUser(currentUser);

        model.addAttribute("successMessageAddBuddy", "Your new buddy is added!");
        loadTransferPageElements(currentUser , model);
        //return "redirect:/transfer?success";
        return "/transfer";
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

    @Transactional
    @PostMapping("/transfer/sendMoney")
    public String sendMoney(@ModelAttribute("transfer") TransferDto transferDto,
                            @AuthenticationPrincipal UserDetails userDetails,
                            Model model, BindingResult result) {

        User currentUser = userService.getUserByEmail(userDetails.getUsername());
        User userSelected = userService.getUserByEmail(transferDto.getBuddyUsername());

        PayMyBuddyBankAccount senderAccount = currentUser.getPayMyBuddyBankAccount();
        double transferAmount = transferDto.getAmount();

        if (userSelected == null){
            result.rejectValue("buddyUsername", null,
                    "Please select a buddy");
        }

        transferService.takeTransferPercentage(transferAmount,senderAccount);
        //TODO : A confirmer, le Transactional empéche le compte d'être débité si la transaction ne se fait pas
        // a cause de "plus assez d'argent sur le compte apres le prélévement des 5%"
        // Dans ce cas est-il necessaire d'ajouter des condition supplémentaire?

        if (transferAmount <= 0) {
            result.rejectValue("amount", null,
                    "Incorrect amount value");

        } else if (senderAccount.getAccountBalance() <= transferAmount) {
            result.rejectValue("amount", null,
                    "you do not have enough money in your account");
        }

        if (result.hasErrors()) {
            loadTransferPageElements(currentUser,model,transferDto);
            return "/transfer";
        }
        Transfer transfer = transferMapper.convertTransferDtoToTransfer(transferDto, currentUser.getEmail());
        transferService.createNewTransfer(transfer);

        bankAccountService.transfer(transfer);
        model.addAttribute("successMessageSendMoney", "Your transfer is sent to " +
                transferDto.getBuddyUsername() + "!");
        loadTransferPageElements(currentUser , model);

        return "/transfer";
    }
}