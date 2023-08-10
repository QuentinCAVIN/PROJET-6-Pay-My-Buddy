package com.paymybuddy.paymybuddysapp.service;

import com.paymybuddy.paymybuddysapp.dto.UserDto;
import com.paymybuddy.paymybuddysapp.model.User;

import java.util.List;

public interface UserService {

    List<User> getUsers();
    List<UserDto> getUsersDto();

    User getUserByEmail(String email);

    User saveUser(UserDto userDto);

    User getUserById(int id);

    void deleteUser(int id);

}

