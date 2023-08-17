package com.paymybuddy.paymybuddysapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



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
    // Les messages
    @Email
    private String email;
    @NotEmpty(message = "Password should not be empty")
    private String password;


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
