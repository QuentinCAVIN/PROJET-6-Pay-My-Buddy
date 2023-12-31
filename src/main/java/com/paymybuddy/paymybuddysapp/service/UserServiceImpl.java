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
        payMyBuddyBankAccount.setAccountBalance(0);

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

    public User getUserByBankAccount(BankAccount bankAccount){
        if (bankAccount instanceof PayMyBuddyBankAccount) {
            return userRepository.findByPayMyBuddyBankAccount((PayMyBuddyBankAccount) bankAccount);
            //utilisation d'un cast pour pouvoir utiliser un objet BankAccount avec un PayMyBuddyBankAccount
        } else {
            return userRepository.findByPersonalBankAccount((PersonalBankAccount) bankAccount);
        }
    }
}