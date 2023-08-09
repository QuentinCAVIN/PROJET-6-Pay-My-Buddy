package com.paymybuddy.paymybuddysapp.service;

import com.paymybuddy.paymybuddysapp.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {

    List<User> getUsers();


    User getUserByEmail(String email);

    User createUser(User user);

    User getUserById(int id);

    void deleteUser(int id);

}

