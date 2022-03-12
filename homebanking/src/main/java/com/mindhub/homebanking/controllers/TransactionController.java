package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.PaymentDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;


@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ClientService clientService;
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CardRepository cardRepository;


    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Object> createTransaction(
            Authentication authentication,
            @RequestParam Double amount,
            @RequestParam String description,
            @RequestParam String sourceAccountNumber,
            @RequestParam String destinationAccountNumber) {
        Client currentClient = clientService.findByEmailClient(authentication.getName());
        List<Account> authenticateSourceAccount = currentClient.getAccounts().stream().collect(toList());

        Account sourceAccount = accountRepository.findByNumber(sourceAccountNumber);
        Account destinationAccount = accountRepository.findByNumber(destinationAccountNumber);

        if (amount == null || description.isEmpty() || sourceAccountNumber.isEmpty() || destinationAccountNumber.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (sourceAccountNumber == destinationAccountNumber) {
            return new ResponseEntity<>("The source account is equal to the destination account", HttpStatus.FORBIDDEN);
        }
        if (sourceAccount == null) {
            return new ResponseEntity<>("The account doesn't exist1", HttpStatus.FORBIDDEN);
        }

        if (!authenticateSourceAccount.contains(sourceAccount)) {
            return new ResponseEntity<>("The account doesn't exist2", HttpStatus.FORBIDDEN);
        }
        if (destinationAccount == null) {
            return new ResponseEntity<>("The account doesn't exist3", HttpStatus.FORBIDDEN);
        }

        if (sourceAccount.getBalance() < amount) {
            return new ResponseEntity<>("Insufficient balance", HttpStatus.FORBIDDEN);
        }
        if ( amount < 0.01) {
            return new ResponseEntity<>("Enter an amount greater than 0", HttpStatus.FORBIDDEN);
        }

        //RESTO SALDO A CUENTA ORIGEN - SUMO SALDO A CUENTA DESTINO
        Double debitAmount = sourceAccount.getBalance() - amount;
        Double creditAmount = destinationAccount.getBalance() + amount;

        LocalDate dateNow = LocalDate.now();
        String description1 = description + "VIN " + destinationAccountNumber;
        String description2 = description + "VIN " + sourceAccountNumber;

        //CREO TRANSACCIONES Y LAS GUARDO EN LA BASE DE DATOS
        Transaction debitTransaction = new Transaction(TransactionType.DEBIT, amount, description1, dateNow, sourceAccount);
        Transaction creditTransaction = new Transaction(TransactionType.CREDIT, amount, description2, dateNow, destinationAccount);

        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);

        //ACTUALIZO EL SALDO DE CADA CUENTA
        sourceAccount.setBalance(debitAmount);
        destinationAccount.setBalance(creditAmount);
        return new ResponseEntity<>("Transaction created!", HttpStatus.CREATED);

    }


}








