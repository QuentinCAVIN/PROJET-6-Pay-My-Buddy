package com.paymybuddy.paymybuddysapp.controller;

import com.paymybuddy.paymybuddysapp.dto.UserDto;
import com.paymybuddy.paymybuddysapp.mapper.UserMapper;
import com.paymybuddy.paymybuddysapp.model.User;
import com.paymybuddy.paymybuddysapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    UserService userService;

    @GetMapping("/home")
    public String home(Model model) { // Spring va fournir une instance de cet objet Model (keske C?)
        List<User> users = userService.getUsers();
        List<UserDto> usersDto = UserMapper.convertUserListToUserDtoList(users);
        model.addAttribute("users", usersDto); //addAttibute va permettre d'ajouter
        // au model, un objet. Le premier paramètre c'est le nom de l'objet sur la page html
        // ,le second c'est l'objet.
        //c'est grace à ça, et avec Thymeleaf, qu'on va pouvoir utiliser les objets dans le html
        //en utilisant les noms de l'objet ${users}

        return "home";
    }
}
