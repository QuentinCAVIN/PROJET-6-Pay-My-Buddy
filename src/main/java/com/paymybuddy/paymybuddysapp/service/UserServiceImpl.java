package com.paymybuddy.paymybuddysapp.service;

import com.paymybuddy.paymybuddysapp.model.User;
import com.paymybuddy.paymybuddysapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{


    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    public UserServiceImpl (UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserById(int id){
        return userRepository.getReferenceById(id);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    //Responsable du cryptage du mot de passe
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode( user.getPassword()));
        return userRepository.save(user);
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }
}
