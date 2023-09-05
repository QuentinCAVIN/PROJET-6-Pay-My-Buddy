package com.paymybuddy.paymybuddysapp.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//Remplace le @data + génère tous les constructeurs possibles

@Entity
@Table(name = "user")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id") //permet d'éviter les boucles infinis quand on vient récupérer un user
// qui a deux attributs List <User>
// https://www.baeldung.com/jackson-bidirectional-relationships-and-infinite-recursion

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable=false, unique=true)
    //Annotation JPA, utile pour construire automatiquement la BDD
    private String email;

    @Column(name = "first_name",nullable=false)
    private String firstName;

    @Column(name = "last_name",nullable = false)
    private String lastName;

    @Column(nullable=false)
    private String password;

    @ManyToMany(
            fetch = FetchType.LAZY, /*Quand on récupère un user, les users associés ne sont pas récupérés.
             (gain de performance) */
            cascade = {CascadeType.PERSIST, CascadeType.MERGE } /*Les modifs sur user sont propagées
                     sur les user associées, uniquement pour création et modif. Si on supprime un user
                     ça ne supprime pas les autres (c'est le cas pour CascadeType.ALL)*/
    )
    @JoinTable(
            name = "user_user",// la table de jointure des relations ManyToMany dans la BDD
            joinColumns = @JoinColumn(name = "user1_id"), //clé étrangère dans la table de jointure
            inverseJoinColumns = @JoinColumn(name = "user2_id"),//clé étrangère de la seconde entité concernée
            uniqueConstraints = @UniqueConstraint(columnNames = {"user1_id", "user2_id"})
            //Va empêcher la création d'un doublon dans la table.
    )

    private List<User> usersConnexions =new ArrayList<>();


    @ManyToMany(
            mappedBy = "usersConnexions",// pas besoin d'ajouter @JoinTable une seconde fois
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE }
    )
    private List<User> usersConnected = new ArrayList<>();


    @OneToOne(
            cascade = CascadeType.ALL, //Supprimer un utilisateur supprimera sont compte associé
            orphanRemoval = true, //garantit la non existance de compte orphelin
            fetch = FetchType.EAGER // Quand on récupére un User on récupére le compte associé
    )
    @JoinColumn(name ="paymybuddy_bank_account_id" )
    private PayMyBuddyBankAccount payMyBuddyBankAccount;


    @OneToOne(
            cascade = CascadeType.ALL, //Supprimer un utilisateur supprimera son compte associé
            orphanRemoval = true, //garantit la non existance de compte orphelin
            fetch = FetchType.EAGER // Quand on récupére un User on récupére le compte associé
    )
    @JoinColumn(name ="personal_bank_account_id" )
    private PersonalBankAccount personalBankAccount;



    //Ci-dessous les méthodes utilitaire (helpers methods)
    //aide à la synchronisation des objets
    //elles sont placées soit du coté OneToMany (la où on gère la liste d'élément)
    //soit du côté ou il y a le @JoinTable pour ManytoMany (un seul coté ici vu que user est lié à lui-même)
    public void addConnexion (User user) {
        usersConnexions.add(user);
        user.getUsersConnected().add(this);
    }

    public void removeConnexion (User user){
        usersConnexions.remove(user);
        user.getUsersConnected().remove(this);
    }

    public void addPayMyBuddyBankAccount(PayMyBuddyBankAccount payMyBuddyBankAccount){
        this.payMyBuddyBankAccount = payMyBuddyBankAccount;
    }

    public void addPersonalBankAccount(PersonalBankAccount personalBankAccount){
        this.personalBankAccount = personalBankAccount;
    }
}
