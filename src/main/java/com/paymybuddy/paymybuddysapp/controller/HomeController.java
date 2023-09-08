package com.paymybuddy.paymybuddysapp.controller;

import com.paymybuddy.paymybuddysapp.dto.BankAccountDto;
import com.paymybuddy.paymybuddysapp.dto.TransferDto;
import com.paymybuddy.paymybuddysapp.dto.UserDto;
import com.paymybuddy.paymybuddysapp.mapper.BankAccountMapper;
import com.paymybuddy.paymybuddysapp.mapper.UserMapper;
import com.paymybuddy.paymybuddysapp.model.*;
import com.paymybuddy.paymybuddysapp.service.BankAccountService;
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

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        User currentUser = userService.getUserByEmail(userDetails.getUsername());

        //To add a new personal account
        // To display the amount of the personal account
        // to display the amount of the pay my buddy account
        loadHomePageElementsToDisplay(currentUser, model);

        //To exchange money between the two accounts
        TransferDto transferDto = new TransferDto();
        model.addAttribute("transfer", transferDto);

        return "home";
    }

    @Transactional
    @PostMapping("/home/linkPersonalBankAccount")
    public String linkPersonalBankAccount(@AuthenticationPrincipal UserDetails userDetails,
                                          @ModelAttribute("personalBankAccount") BankAccountDto personalBankAccountDto,
                                          Model model) {
        User curentUser = userService.getUserByEmail(userDetails.getUsername());

        PersonalBankAccount personalBankAccount =
                BankAccountMapper.convertBankAccountDtoToPersonalBankAccount(personalBankAccountDto);

        bankAccountService.linkNewPersonalBankAccount(personalBankAccount, curentUser);

        return "redirect:/home?success";
    }

    @Transactional
    @PostMapping("/home/creditAndCashIn")
    public String creditAndCashIn(@AuthenticationPrincipal UserDetails userDetails,
                                  @RequestParam String button, @ModelAttribute("transfer") TransferDto transferDto, Model model,
                                  BindingResult result) {

        User currentUser = userService.getUserByEmail(userDetails.getUsername());
        PayMyBuddyBankAccount payMyBuddyBankAccount = currentUser.getPayMyBuddyBankAccount();
        PersonalBankAccount personalBankAccount = currentUser.getPersonalBankAccount();

        Transfer transfer = new Transfer();
        double transferAmount = transferDto.getAmount();
        transfer.setAmount(transferAmount);

        if (transferAmount <= 0) {
            result.rejectValue("amount", null,
                    "Incorrect amount value");
        }

        if (button.equals("credit")) {

            if (personalBankAccount.getAccountBalance() < transferAmount) {
                result.rejectValue("amount", null,
                        "you do not have enough money in your personal account. Wait for payday");
            } else {
                transfer.setSenderAccount(personalBankAccount);
                transfer.setRecipientAccount(payMyBuddyBankAccount);
            }

        } else if ("cashIn".equals(button)) {

            if (payMyBuddyBankAccount.getAccountBalance() < transferAmount) {
                result.rejectValue("amount", null,
                        "you do not have enough money in your Pay My Buddy account. We don't give credit");
            } else {
                transfer.setSenderAccount(payMyBuddyBankAccount);
                transfer.setRecipientAccount(personalBankAccount);
            }
        }

        if (result.hasErrors()) {
            model.addAttribute("transfer", transferDto);
            loadHomePageElementsToDisplay(currentUser, model);
            return "/home";
        }
        bankAccountService.transfer(transfer);
        return "redirect:/home";
    }

    private void loadHomePageElementsToDisplay(User currentUser, Model model) {

        List<User> buddies = currentUser.getUsersConnexions();
        List<UserDto> buddiesDto = UserMapper.convertUserListToUserDtoList(buddies);
        model.addAttribute("buddies", buddiesDto);

        //To add a new personal account

        BankAccountDto personalBankAccountToFile = new BankAccountDto();
        model.addAttribute("personalBankAccountToFile", personalBankAccountToFile);

        // To display the amount of the personal account
        PersonalBankAccount currentUserPersonalBankAccount = currentUser.getPersonalBankAccount();
        BankAccountDto personalBankAccountToDisplay =
                BankAccountMapper.convertBankAccountToBankAccountDto(currentUserPersonalBankAccount);
        model.addAttribute("personalBankAccountToDisplay", personalBankAccountToDisplay);

        // to display the amount of the pay my buddy account
        PayMyBuddyBankAccount payMyBuddyBankAccount = currentUser.getPayMyBuddyBankAccount();
        BankAccountDto currentUserPayMyBuddyAccount =
                BankAccountMapper.convertBankAccountToBankAccountDto(payMyBuddyBankAccount);
        model.addAttribute("payMyBuddyBankAccount", currentUserPayMyBuddyAccount);
    }
}