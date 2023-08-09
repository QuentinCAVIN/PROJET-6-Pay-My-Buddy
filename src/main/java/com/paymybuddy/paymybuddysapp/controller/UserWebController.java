package com.paymybuddy.paymybuddysapp.controller;

import com.paymybuddy.paymybuddysapp.model.User;
import com.paymybuddy.paymybuddysapp.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
public class UserWebController {


    private UserService userService;

    public UserWebController(UserService userService) { //ça ne peut pas être remplacé par lombock?(@Data)
        this.userService = userService;
    }


    @GetMapping("/index")
    public String home(Model model) {
        Iterable<User> users = userService.getUsers();
        model.addAttribute("users", users);

        return "home";
    }
   @GetMapping("/login")

    public String login() {


        return "login";
    }

    @PostMapping("/registration/saveUser")
    public ModelAndView saveUser(@ModelAttribute User user) {
        //@ModelAttribute permet à Spring de récupérer les données saisi dans un formulaire
        //correctement annoté (<form method="post" th:action="@{/saveUser}" th:object="${user}">).
        //Et donc construire un objet employee avec.

        userService.createUser(user);
        return new ModelAndView("redirect:/index");
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "newAccount";
       //return "registertuto";
    }

    //Methode Tuto
    @GetMapping("/users")
    public String users(Model model){
        Iterable<User> users = userService.getUsers();
        model.addAttribute("users", users);
        return "userstuto";
    }






}/*	th:field="*{email}"
			th:field="*{password}"
                    th:field="*{firstName}"
                    th:field="*{lastName}"
                    th:field="*{dateOfBirt}"
                    th:field="*{streetAndNumber}"
                    th:field="*{zip}"
                    th:field="*{city}"
                    th:field="*{country}"
                    th:field="*{cityOfBirth}"
                    th:field="*{countryOfBirth}"
                    th:field="*{phone}"
                    th:field="*{zipOfBirth}*/

