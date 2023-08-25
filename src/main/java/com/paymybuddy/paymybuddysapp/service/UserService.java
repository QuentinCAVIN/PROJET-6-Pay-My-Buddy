package com.paymybuddy.paymybuddysapp.service;

import com.paymybuddy.paymybuddysapp.model.User;

import java.util.List;

public interface UserService {

    User getUserByEmail(String email);

    List<User> getUsers();

    void createNewUser(User user);

    void saveUser(User user);

    void deleteUser(int id);
}

