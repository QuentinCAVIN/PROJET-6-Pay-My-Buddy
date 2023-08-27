package com.paymybuddy.paymybuddysapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
/*Comme l'annotation @MappedSuperclass ,La classe parente ne sera pas persisté en BDD.
A confirmer : necessaire d'utiliser @GeneratedValue(strategy = GenerationType.AUTO)
JPA va générer une table nom_de_la_classe_seq (bank_account_seq ici), qui va permettre de garantir
un identifiant unique entre mes deux classes qui héritent de BankAccount.
L’id "1" correspondra a un seul BankAccount présent dans l’une ou l’autre des deux tables filles, mais pas les deux */
@Getter
@Setter
public abstract class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "account_balance", nullable = false)
    private double accountBalance;

    @OneToMany(
            mappedBy = "senderAccount",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true
    )
    private List<Transfer> sentTransfers = new ArrayList<>();

    @OneToMany(
            mappedBy = "recipientAccount",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true
    )
    private List<Transfer> receivedTransfers = new ArrayList<>();


    //Ci-dessous les méthodes utilitaire (helpers methods)
    //aide à la synchronisation des objets
    //elles sont placées soit du coté OneToMany (la où on gère la liste d'élément)
    //soit du côté ou il y a le @JoinTable pour ManytoMany
    public void addSentTransfer (Transfer transfer) {
        sentTransfers.add(transfer);
        transfer.setSenderAccount(this);
    }
    public void removeConnexion (Transfer transfer){
        sentTransfers.remove(transfer);
        transfer.setSenderAccount(null);
    }

    public void addReceivedTransfer(Transfer transfer){
        receivedTransfers.add(transfer);
        transfer.setRecipientAccount(this);
    }

    public void removeReceivedTransfer(Transfer transfer){
        receivedTransfers.remove(transfer);
        transfer.setRecipientAccount(null);
    }

}

/*https://www.baeldung.com/hibernate-inheritance*/

/*https://thorben-janssen.com/complete-guide-inheritance-strategies-jpa-hibernate*/