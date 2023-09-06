package com.paymybuddy.paymybuddysapp.dto;

import com.paymybuddy.paymybuddysapp.model.PayMyBuddyBankAccount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReducedUserDto {
    // The ReducedUserDto class is used to replace the List<User>connections attribute of the User class
    //by List<ReducedUserDto>connections of class UserDto

    private int id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private PayMyBuddyBankAccount payMyBuddyBankAccount;
}
