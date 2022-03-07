package com.mindhub.homebanking.models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String number;
    private LocalDateTime creationDate = LocalDateTime.now();
    private Double balance;
    private AccountType type;
    private boolean accountStatus = true;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Client_id")
    private Client client;

    @OneToMany(mappedBy="account", fetch=FetchType.EAGER)
    Set<Transaction> transaction = new HashSet<>();

        public Account() {
    }

    public Account( String number, LocalDateTime creationDate, Double balance, Client client, AccountType type, boolean accountStatus) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
        this.client = client;
        this.type = type;
    }

    public long getId() {
        return id;
    }


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public boolean isAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(boolean accountStatus) {
        this.accountStatus = accountStatus;
    }

    @JsonIgnore
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<Transaction> getTransaction() {
        return transaction;
    }

    @JsonIgnore
    public void setTransaction(Set<Transaction> transaction) {
        this.transaction = transaction;
    }

    public void addTransactions(Transaction transaction) {
        transaction.setAccount(this);
    }


}
