package com.paymybuddy.paymybuddysapp.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

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

    @ManyToMany(
            fetch = FetchType.LAZY, /*Quand on récupère un user, les users associés ne sont pas récupérés.
             (gain de performance) */
            //TODO: voir si "fetch = FetchType.EAGER" ne serait pas plus approprié.
            cascade = {CascadeType.PERSIST, CascadeType.MERGE } /*Les modifs sur user sont propagées
                     sur les user associées, uniquement pour création et modif. Si on supprime un user
                     ça ne supprime pas les autres (c'est le cas pour CascadeType.ALL)*/
    )
    @JoinTable(
            name = "user_user",// la table de jointure des relations ManyToMany dans la BDD
            joinColumns = @JoinColumn(name = "user1_id"), //clé étrangère dans la table de jointure
            inverseJoinColumns = @JoinColumn(name = "user2_id")//clé étrangère de la seconde entité concernée
    )
    private List<User> usersConnexions;

    @ManyToMany(
            mappedBy = "usersConnexions",// pas besoin d'ajouter @JoinTable une seconde fois
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE }
    )
    private List<User> usersConnected;


    //Ci-dessous les méthodes utilitaire (helpers methods)
    //aide à la synchronisation des objets
    //elles sont placés soit du coté OneToMany (la où on gère la liste d'élément)
    //soit du côté ou il y a le @JoinTable pour ManytoMany (un seul coté ici vu que user est lié à lui-même)
    public void addConnexion (User user) {
        usersConnexions.add(user);
        user.getUsersConnected().add(this);
    }

    public void removeConnexion (User user){
        usersConnexions.remove(user);
        user.getUsersConnected().remove(this);
    }

}
