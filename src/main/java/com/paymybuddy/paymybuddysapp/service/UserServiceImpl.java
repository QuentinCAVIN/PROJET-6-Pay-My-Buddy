package com.paymybuddy.paymybuddysapp.service;

import com.paymybuddy.paymybuddysapp.dto.UserDto;
import com.paymybuddy.paymybuddysapp.model.BankAccount;
import com.paymybuddy.paymybuddysapp.model.PayMyBuddyBankAccount;
import org.hibernate.Hibernate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Service;
import com.paymybuddy.paymybuddysapp.model.User;
import com.paymybuddy.paymybuddysapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {


    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserDto getUserDtoByEmail(String email) {

        User user = userRepository.findByEmail(email);

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setPassword(user.getPassword());
        userDto.setUsersConnexions(user.getUsersConnexions());
        userDto.setPayMyBuddyBankAccount(user.getPayMyBuddyBankAccount());

        return userDto;

        //TODO : Rajouter un mapper (=convertisseur) de userDto dans un package exterieur UserMapper
    }


    public List<UserDto> getUsersDto() {
        List<User> users = userRepository.findAll();
        List<UserDto> usersDto = new ArrayList<>();
        users.forEach(user -> {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setEmail(user.getEmail());
            userDto.setFirstName(user.getFirstName());
            userDto.setLastName(user.getLastName());
            userDto.setPassword(user.getPassword());
            userDto.setUsersConnexions(user.getUsersConnexions());
            usersDto.add(userDto);
            userDto.setUsersConnexions(user.getUsersConnexions());
            userDto.setPayMyBuddyBankAccount(user.getPayMyBuddyBankAccount());
        });
        return usersDto;
    }

    public void createNewUser(UserDto userDto) {
        User user = new User();

        PayMyBuddyBankAccount payMyBuddyBankAccount= new PayMyBuddyBankAccount();
        payMyBuddyBankAccount.setAccountBalance(0);

        user.addPayMyBuddyBankAccount(payMyBuddyBankAccount);
        user.setId(userDto.getId());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        saveUser(user);
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }



    public void saveUser(User user) {
        userRepository.save(user);
    }

    // TODO: Les deux classes ci dessous sont peut être inutile, il suffit de faire un User.addConnexion(Use)
    public void addConnexion(String currentUserEmail, /* remplacer par User CurrentUser avec @authenticationprincipal */
                             String emailUserToAdd) {

        User currentUser = getUserByEmail(currentUserEmail);

        User userToAdd = getUserByEmail(emailUserToAdd);

        currentUser.addConnexion(userToAdd);
    }

    public void removeConnexion(String currentUserEmail, String emailUserToRemove) {

        User currentUser = getUserByEmail(currentUserEmail);

        User userToRemove = getUserByEmail(emailUserToRemove);

        currentUser.removeConnexion(userToRemove);
    }
}
