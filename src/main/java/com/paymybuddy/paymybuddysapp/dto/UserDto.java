package com.paymybuddy.paymybuddysapp.dto;

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

    private List<User> usersConnexions =new ArrayList<>(); //TODO: modifier les tests unitaires pour prendre en compte ce nouvel attribut


    @Override
    public String toString(){
    return this.email;
    }
    @Override
    public boolean equals(Object userDto){
        return (userDto.toString() == this.toString());
    }
}
// TODO : en fin de projet refléchir à l'utilité du champ id sur UserDto

// TODO les methode toString et equals servent uniquement pour les tests. Les supprimer?
