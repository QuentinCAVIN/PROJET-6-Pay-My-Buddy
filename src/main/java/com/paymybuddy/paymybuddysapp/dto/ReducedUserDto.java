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

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private PayMyBuddyBankAccount payMyBuddyBankAccount;
}
