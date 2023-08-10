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
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotEmpty(message = "Email should not be empty")
    // Les messages
    @Email
    private String email;
    @NotEmpty(message = "Password should not be empty")
    private String password;
}
