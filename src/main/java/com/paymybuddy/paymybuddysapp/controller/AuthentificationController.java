package com.paymybuddy.paymybuddysapp.controller;

import com.paymybuddy.paymybuddysapp.dto.UserDto;
import com.paymybuddy.paymybuddysapp.mapper.UserMapper;
import com.paymybuddy.paymybuddysapp.model.User;
import com.paymybuddy.paymybuddysapp.service.UserService;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthentificationController {

    private UserService userService;
    UserMapper userMapper = new UserMapper();

    public AuthentificationController(UserService userService) { //ça ne peut pas être remplacé par lombock?(@Data)
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "newAccount";
    }

    @PostMapping("/registration/saveUser")
    public String saveUser(@Valid @ModelAttribute("user") UserDto userDto,
                           BindingResult result, Model model) {

        User optionalUser = userService.getUserByEmail(userDto.getEmail());

        if (optionalUser != null) {
            result.rejectValue("email", null,
                    "There is already an account associated with the "
                            + optionalUser.getEmail() + " email.");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "/newAccount";
        }

        User newUser = userMapper.convertUserDtotoUser(userDto);
        userService.createNewUser(newUser);
        return "redirect:/login?success";
    }
}
