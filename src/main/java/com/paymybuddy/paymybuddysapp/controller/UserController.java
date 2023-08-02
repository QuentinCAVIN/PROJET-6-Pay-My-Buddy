package com.paymybuddy.paymybuddysapp.controller;

import com.paymybuddy.paymybuddysapp.model.User;
import com.paymybuddy.paymybuddysapp.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {

        Optional<User> userAlreadyPresent = userService.getUserByEmail(user.getEmail());

        if (userAlreadyPresent.get() == null) {
            throw new NoSuchElementException();
            //TODO: voir si necessaire d'implémenter des classes d'erreur custom
        }
        User userSaved = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userSaved);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") int id, @Valid @RequestBody User user) {

        Optional<User> optionalUser = userService.getUserById(id);
        if (optionalUser.isPresent()) {

            User currentUser = optionalUser.get();

            currentUser.setFirstName(user.getFirstName());
            currentUser.setLastName(user.getLastName());
            currentUser.setPassword(user.getPassword());
            currentUser.setDateOfBirth(user.getDateOfBirth());
            currentUser.setStreetAndNumber(user.getStreetAndNumber());
            currentUser.setZip(user.getZip());
            currentUser.setCity(user.getCity());
            currentUser.setCountry(user.getCountry());
            currentUser.setCityOfBirth(user.getCityOfBirth());
            currentUser.setCountryOfBirth(user.getCountryOfBirth());
            currentUser.setPhone(user.getPhone());
            currentUser.setZipOfBirth(user.getZipOfBirth());
            currentUser.setEmail(user.getEmail());
            currentUser.setUsersConnexions(user.getUsersConnexions());
            currentUser.setUsersConnected(user.getUsersConnected());
            //TODO voir si il est possible de faire une requete put en entrant uniquement les champs à modifier
            // (et non l'intégralité des champs de l'objet). Dans ce cas prévoir
            // Sinon prévoir un (if userAttribut != null) -> set userAttribut

            User userSaved = userService.createUser(currentUser);

            return ResponseEntity.status(HttpStatus.OK).body(userSaved);

        } else {
            throw new NoSuchElementException();
        }
    }

    @Transactional
    @DeleteMapping("/user/{id}")
    public ResponseEntity deleteFireStation(@PathVariable("id") int id) {

        Optional<User> optionalUser = userService.getUserById(id);
        if (optionalUser.isPresent()) {

            userService.deleteUser(id);
            return ResponseEntity.noContent().build();

        } else {
            throw new NoSuchElementException();
        }
    }
}



