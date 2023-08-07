package com.paymybuddy.paymybuddysapp.web.controller;

import com.paymybuddy.paymybuddysapp.model.User;
import com.paymybuddy.paymybuddysapp.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Data
@Controller
public class UserWebController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String login() {

        return "login";
    }

    @PostMapping("/saveUser")
    public ModelAndView saveUser(@ModelAttribute User user) {
        //@ModelAttribute permet à Spring de récupérer les données saisi dans un formulaire
        //correctement annoté (<form method="post" th:action="@{/saveEmployee}" th:object="${employee}">).
        //Et donc construire un objet employee avec.

        userService.createUser(user);
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/createUser")
    public String createUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "newAccount";
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

