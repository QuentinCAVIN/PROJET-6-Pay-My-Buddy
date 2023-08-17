package com.paymybuddy.paymybuddysapp.service;

import com.paymybuddy.paymybuddysapp.dto.UserDto;
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

        return userDto;
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
            usersDto.add(userDto);
        });
        return usersDto;
    }


    //Responsable du cryptage du mot de passe
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }
}
