package com.paymybuddy.paymybuddysapp.controller;

import com.paymybuddy.paymybuddysapp.model.User;
import com.paymybuddy.paymybuddysapp.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class UserWebController {

    private UserService userService;

    public UserWebController(UserService userService) { //ça ne peut pas être remplacé par lombock?(@Data)
        this.userService = userService;
    }

    @GetMapping("/home")
    public String home(Model model) { // Spring va fournir une instance de cet objet Model (keske C?)
        Iterable<User> users = userService.getUsers();
        model.addAttribute("users", users); //addAttibute va permettre d'ajouter
        // au model, un objet. Le premier paramètre c'est le nom de l'objet sur la page html
        // ,le second c'est l'objet.
        //c'est grace à ça, et avec Thymeleaf, qu'on va pouvoir utiliser les objets dans le html
        //en utilisant les noms de l'objet ${users}

        return "home";
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "newAccount";
    }

    @PostMapping("/registration/saveUser")
    public ModelAndView saveUser(@ModelAttribute User user) {
        //@ModelAttribute permet à Spring de récupérer les données saisis dans un formulaire
        //correctement annoté (<form method="post" th:action="@{/saveUser}" th:object="${user}">).
        //Et donc construire un objet user avec.

        userService.createUser(user);
        return new ModelAndView("redirect:/index");
    }

    @GetMapping("/deleteUser/{id}")
    public ModelAndView deleteUser(@PathVariable("id") final int id) {
        userService.deleteUser(id);
        return new ModelAndView("redirect:/home");
    } //TODO : la methode ne fonctionne pas sur les utilisateurs associé a d'autres utilisateur (CASCADE RESTRICT)


    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
