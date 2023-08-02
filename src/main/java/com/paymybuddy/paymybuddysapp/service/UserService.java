package com.paymybuddy.paymybuddysapp.service;

import com.paymybuddy.paymybuddysapp.model.User;
import com.paymybuddy.paymybuddysapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {

    Iterable<User> getUsers();

    Optional<User> getUserById(int id);


    Optional<User> getUserByEmail(String email);

    User createUser(User user);

    void deleteUser(int id);

}

