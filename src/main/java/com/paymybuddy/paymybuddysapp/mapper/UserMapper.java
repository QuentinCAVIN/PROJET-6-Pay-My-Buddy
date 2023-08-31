package com.paymybuddy.paymybuddysapp.mapper;

import com.paymybuddy.paymybuddysapp.dto.ReducedUserDto;
import com.paymybuddy.paymybuddysapp.dto.UserDto;
import com.paymybuddy.paymybuddysapp.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    public static UserDto convertUsertoUserDto(User user) {

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setPassword(user.getPassword());
        userDto.setPayMyBuddyBankAccount(user.getPayMyBuddyBankAccount());
        userDto.setPersonalBankAccount(user.getPersonalBankAccount());

        //convert List<User> getUsersConnexion to List<ReducedUserDto> getUsersConnexions
        List<ReducedUserDto> reducedUsersDto = new ArrayList<>();

        List<User> userConnexions = user.getUsersConnexions();
        if (userConnexions != null && !userConnexions.isEmpty()){
        user.getUsersConnexions().forEach(buddy ->reducedUsersDto.add(convertUserToReducedUserDto(buddy)));
        userDto.setUsersConnexions(reducedUsersDto);
        }

        return userDto;
    }

    public static List<UserDto> convertUserListToUserDtoList(List<User> users) {

        List<UserDto> usersDto = new ArrayList<>();

        users.forEach(user -> {
            UserDto userDto = convertUsertoUserDto(user);
            usersDto.add(userDto);
        });

        return usersDto;
    }

    public static User convertUserDtotoUser(UserDto userDto) {

        User user = new User();

        user.setId(userDto.getId());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(userDto.getPassword());
        user.setPayMyBuddyBankAccount(userDto.getPayMyBuddyBankAccount());
        user.setPersonalBankAccount(userDto.getPersonalBankAccount());
        // TODO : la methode ne transforme pas List<ReducedUser> UsersConnexions trop compliqué pour moi,
        //  et inutile pour le moment. Modifier si besoin.

        return user;
    }


    // TODO: methode inutilisée pour le moment. L'effacer en fin de projet si pas utilisé
  /*  public static List<User> convertUserDtoListToUserList(List<UserDto> usersDto) {

        List<User> users = new ArrayList<>();

        usersDto.forEach(userDto -> {
            User user = convertUserDtotoUser(userDto);
            users.add(user);
        });

        return users;
    }*/



    //TODO: Pas de test ci dessous
    public static ReducedUserDto convertUserToReducedUserDto(User user){

        ReducedUserDto reducedUserDto = new ReducedUserDto();
        reducedUserDto.setId(user.getId());
        reducedUserDto.setEmail(user.getEmail());
        reducedUserDto.setFirstName(user.getFirstName());
        reducedUserDto.setLastName(user.getLastName());
        reducedUserDto.setPassword(user.getPassword());
        reducedUserDto.setPayMyBuddyBankAccount(user.getPayMyBuddyBankAccount());

        return reducedUserDto;
    }

 /*   public static  User convertReducedUserDtoToUser(ReducedUserDto reducedUserDto){
        User user = new User();
        user.setId(reducedUserDto.getId());
        user.setEmail(reducedUserDto.getEmail());
        user.setFirstName(reducedUserDto.getFirstName());
        user.setLastName(reducedUserDto.getLastName());
        user.setPassword(reducedUserDto.getPassword());
        user.setPayMyBuddyBankAccount(reducedUserDto.getPayMyBuddyBankAccount());

        // Je n'ai pas réglé l'attribut connexions et connected
        return user;
        //TODO : la classe convertUserDtotoUser n'a pas besoin pour le moment de fournir de
        // l'attribut  List<User>Connexions de User. Et donc pas besoin de convertReducedUserDtoToUser
    }*/
}