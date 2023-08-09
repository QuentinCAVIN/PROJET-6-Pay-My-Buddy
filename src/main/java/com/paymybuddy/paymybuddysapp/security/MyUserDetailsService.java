package com.paymybuddy.paymybuddysapp.security;

import com.paymybuddy.paymybuddysapp.model.User;
import com.paymybuddy.paymybuddysapp.repository.UserRepository;
import com.paymybuddy.paymybuddysapp.service.UserService;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class MyUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;
    //J'utilise useRepository plutôt que UserService pour régler un problème de boucle de Beans

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    /*Quand l'utilisateur entre son adresse mail, Spring va créer une instance de UserDetail à partir des informations
     présente dans la BDD et vérifier que le mot de passe saisi par l'utilisateur
      correspond bien au mot de passe de UserDetail*/
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("No user found with username: " + email);
        }
        // TODO: modifier la méthode en fin de projet en fonction des besoins de role:

        //Si besoin d'utiliser plusieurs roles, à supprimer
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        //

        //+ remplacer authorities par "getAuthorities(user.getRoles())"
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), authorities);
    }

    //Methode qui va servir à convertir une liste de String en liste de GrantedAuthority.
    //Nécessaire pour créer un UserDetails user
    private static List<GrantedAuthority> getAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
}

