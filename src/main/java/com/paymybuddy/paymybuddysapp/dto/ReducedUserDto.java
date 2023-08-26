package com.paymybuddy.paymybuddysapp.dto;

import com.paymybuddy.paymybuddysapp.model.PayMyBuddyBankAccount;
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
public class ReducedUserDto{
        // La classe ReducedUserDto sert a remplacer l'attribut List<User>connexions de la classe User
        //par List<ReducedUserDto>connexions de la classe UserDto

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

    private PayMyBuddyBankAccount payMyBuddyBankAccount;
}
