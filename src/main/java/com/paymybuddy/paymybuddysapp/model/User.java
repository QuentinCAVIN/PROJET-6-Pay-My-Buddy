package com.paymybuddy.paymybuddysapp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String password;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "number_and_street")
    private String streetAndNumber;

    private int zip;

    private String city;

    private String country;

    @Column(name = "city_of_birth")
    private String cityOfBirth;

    @Column(name = "country_of_birth")
    private String countryOfBirth;

    private String phone;

    @Column(name = "zip_of_birth")
    private int zipOfBirth;

    private String email;

}
