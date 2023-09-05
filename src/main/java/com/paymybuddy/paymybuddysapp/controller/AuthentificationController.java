package com.paymybuddy.paymybuddysapp.controller;

import com.paymybuddy.paymybuddysapp.dto.UserDto;
import com.paymybuddy.paymybuddysapp.mapper.UserMapper;
import com.paymybuddy.paymybuddysapp.model.User;
import com.paymybuddy.paymybuddysapp.service.UserService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import java.util.List;


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
                           //@ModelAttribute permet à Spring de récupérer les données saisies dans un formulaire
                           //correctement annoté (<form method="post" th:action="@{/saveUser}" th:object="${user}">).
                           //Et donc construire un objet user avec. Le nom doit correspondre au nom du UserDto vide
                           //"chargé" dans la page html par "showRegistrationForm".

                           BindingResult result,
                           //Sert à collecter et gérer les résultats des @Valid

                           Model model) {

        User optionalUser = userService.getUserByEmail(userDto.getEmail());

        if (optionalUser != null) {//J'ai vu un exemple que je ne comprends pas du tout :
            // if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty())

            result.rejectValue("email", null,
                    "There is already an account associated with the "
                    + optionalUser.getEmail() + " email."); // Spring va rejeter la valeur associée au champ email
            // présent dans le formulaire
        }

        if (result.hasErrors()) { // On recharge la page en cas d'erreur
            //Spring ajoute automatiquement l'objet BindingResult au model
            //la nouvelle page prendra les erreurs en compte(th:errors).

            model.addAttribute("user", userDto);
            //On conserve les informations saisies en ajoutant le userDto courant,

            return "/newAccount";

        }

        User newUser = userMapper.convertUserDtotoUser(userDto);
        userService.createNewUser(newUser);
        return "redirect:/login?success";
        //On recharge la page
    }

    // TODO : Methode non testée pour l'instant, a Intégrer d'abord au bonne endroit
    @Transactional
    @GetMapping("/deleteUser/{id}")
    public ModelAndView deleteUser(@PathVariable("id") final int id) {
        userService.deleteUser(id);
        return new ModelAndView("redirect:/home");
    }

}
