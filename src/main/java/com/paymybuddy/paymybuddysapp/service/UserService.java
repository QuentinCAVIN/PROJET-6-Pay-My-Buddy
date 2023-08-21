package com.paymybuddy.paymybuddysapp.service;

import com.paymybuddy.paymybuddysapp.dto.UserDto;
import com.paymybuddy.paymybuddysapp.model.User;

import java.util.List;

public interface UserService {

    List<UserDto> getUsersDto();

    User getUserByEmail(String email);

    UserDto getUserDtoByEmail(String email);

    void saveUserDto(UserDto userDto);

    void saveUser(User user);

    void deleteUser(int id);

}

