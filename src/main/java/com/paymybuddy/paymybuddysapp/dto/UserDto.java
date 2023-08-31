package com.paymybuddy.paymybuddysapp.dto;

import com.paymybuddy.paymybuddysapp.model.PayMyBuddyBankAccount;
import com.paymybuddy.paymybuddysapp.model.PersonalBankAccount;
import com.paymybuddy.paymybuddysapp.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto
        // La classe UserDto sert a faire le transfert entre la la couche controller et la vue
{
    private int id;

    @NotEmpty(message = "First name should not be empty")
    private String firstName;

    @NotEmpty(message = "Last name should not be empty")
    private String lastName;

    @NotEmpty(message = "Email should not be empty")
    // Les messages s'affichent dans le champ <th:errors = "*{email}"> de la page html
    @Email
    private String email;

    @NotEmpty(message = "Password should not be empty")
    private String password;

    private List<ReducedUserDto> usersConnexions = new ArrayList<>(); //Todo convertir en liste de UserDto

    private PayMyBuddyBankAccount payMyBuddyBankAccount;

    private PersonalBankAccount personalBankAccount;
}