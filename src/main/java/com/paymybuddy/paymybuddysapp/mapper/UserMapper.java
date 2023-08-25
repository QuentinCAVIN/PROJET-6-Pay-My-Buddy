package com.paymybuddy.paymybuddysapp.mapper;

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
        userDto.setUsersConnexions(user.getUsersConnexions());
        userDto.setPayMyBuddyBankAccount(user.getPayMyBuddyBankAccount());

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
        user.setUsersConnexions(userDto.getUsersConnexions());
        user.setPayMyBuddyBankAccount(userDto.getPayMyBuddyBankAccount());

        return user;
    }

    public static List<User> convertUserDtoListToUserList(List<UserDto> usersDto) {

        List<User> users = new ArrayList<>();

        usersDto.forEach(userDto -> {
            User user = convertUserDtotoUser(userDto);
            users.add(user);
        });

        return users;
    }
}