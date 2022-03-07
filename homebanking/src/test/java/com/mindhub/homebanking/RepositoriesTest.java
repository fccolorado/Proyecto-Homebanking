package com.mindhub.homebanking;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

//@DataJpaTest
@SpringBootTest
@AutoConfigureTestDatabase(replace = NONE)
public class RepositoriesTest {

    @Autowired
    LoanRepository loanRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    ClientLoanRepository clientLoanRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @Test
    public void existLoans(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans,is(not(empty())));
    }
    @Test
    public void existLoanPersonal() {
        List<Loan> loans= loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
    }

    @Test
    public void existClients(){
        List<Client> client = clientRepository.findAll();
        assertThat(client,is(not(empty())));
    }
    @Test
    public void existClientMelba() {
        List<Client> client = clientRepository.findAll();
        assertThat(client, hasItem(hasProperty("firstName", is("Melba"))));
        assertThat(client, hasItem(hasProperty("lastName", is("Morel"))));
    }
    @Test
    public void existAccount(){
        List<Account> account = accountRepository.findAll();
        assertThat(account,is(not(empty())));
    }
    @Test
    public void existAccounts() {
        List<Account> account = accountRepository.findAll();
        assertThat(account, hasItem(hasProperty("number", is("VIN001"))));
    }
    @Test
    public void existCard(){
        List<Card> card= cardRepository.findAll();
        assertThat(card,is(not(empty())));
    }
    @Test
    public void existCards() {
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, hasItem(hasProperty("number", is("5456 5834 5674 8082"))));
    }
    @Test
    public void existTransaction(){
        List<Transaction> transaction = transactionRepository.findAll();
        assertThat(transaction,is(not(empty())));
    }
    @Test
    public void existTransactions() {
        List<Transaction> transaction  =  transactionRepository.findAll();
        assertThat(transaction, hasItem(hasProperty("description", is("Payment PSE"))));
    }



}