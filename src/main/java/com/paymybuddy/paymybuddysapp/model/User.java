package com.paymybuddy.paymybuddysapp.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String password;

    @ManyToMany(
            fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    @JoinTable(
            name = "user_user",
            joinColumns = @JoinColumn(name = "user1_id"),
            inverseJoinColumns = @JoinColumn(name = "user2_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user1_id", "user2_id"})
    )

    private List<User> usersConnexions = new ArrayList<>();

    @ManyToMany(
            mappedBy = "usersConnexions",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    private List<User> usersConnected = new ArrayList<>();

    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "paymybuddy_bank_account_id", nullable = false)
    private PayMyBuddyBankAccount payMyBuddyBankAccount;

    @OneToOne(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @JoinColumn(name = "personal_bank_account_id")
    private PersonalBankAccount personalBankAccount;

    public void addConnexion(User user) {
        usersConnexions.add(user);
        user.getUsersConnected().add(this);
    }

    public void removeConnexion(User user) {
        usersConnexions.remove(user);
        user.getUsersConnected().remove(this);
    }

    public void addPayMyBuddyBankAccount(PayMyBuddyBankAccount payMyBuddyBankAccount) {
        this.payMyBuddyBankAccount = payMyBuddyBankAccount;
    }

    public void addPersonalBankAccount(PersonalBankAccount personalBankAccount) {
        this.personalBankAccount = personalBankAccount;
    }
}