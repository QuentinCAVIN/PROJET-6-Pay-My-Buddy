package com.paymybuddy.paymybuddysapp.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;

import org.springframework.security.config.web.server.ServerSecurityMarker;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;



@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(csrf -> csrf.disable());
        return http.build();
    }
    //TODO: Dans la doc de Spring j'ai trouvé l'utilisation de SecurityWebFilterChain qui prend en paramètre
    // ServerHttpSecurity et SecurityFilterChain qui prend en paramètre HttpSecurity.
    // Cela correspondrait au "modèle Servlet" et "modèle Réactive"
    //https://docs.spring.io/spring-security/reference/reactive/exploits/csrf.html
    //https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#servlet-csrf-configure-disable
}
