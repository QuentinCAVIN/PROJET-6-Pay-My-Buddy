package com.paymybuddy.paymybuddysapp.service;

import com.paymybuddy.paymybuddysapp.mapper.UserMapper;
import com.paymybuddy.paymybuddysapp.model.*;
import org.springframework.stereotype.Service;
import com.paymybuddy.paymybuddysapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {


    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;



    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        return user;
    }

    public void createNewUser(User user) {

        PayMyBuddyBankAccount payMyBuddyBankAccount = new PayMyBuddyBankAccount();
        payMyBuddyBankAccount.setAccountBalance(1000); // TODO modifier à 0 quand les test seront finis

        user.addPayMyBuddyBankAccount(payMyBuddyBankAccount);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        saveUser(user);
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    //TODO: Créé mais pas utilisé pour l'instant
    public User getUserByPayMyBuddyBankAccount(PayMyBuddyBankAccount payMyBuddyBankAccount){
        return userRepository.findByPayMyBuddyBankAccount(payMyBuddyBankAccount);
    }

}
