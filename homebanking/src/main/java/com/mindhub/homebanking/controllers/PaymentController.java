package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.PaymentDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.List;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class PaymentController {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CardRepository cardRepository;

    @Transactional
    @PostMapping("/payment")
    public ResponseEntity<Object> createTransaction(Authentication authentication, @RequestBody PaymentDTO paymentDTO) {
        Client currentClient = clientRepository.findByEmail(authentication.getName());
        Card card = cardRepository.findByNumber(paymentDTO.getNumber());
        List<Account> account = currentClient.getAccounts().stream().collect(toList());
        List<Account> accountPayment = account.stream().filter(account1 -> account1.getBalance() > paymentDTO.getAmount()).collect(toList());

        if (paymentDTO == null) {
            return new ResponseEntity<>("Mising data", HttpStatus.FORBIDDEN);
        }
        if (paymentDTO.getNumber().isEmpty() || card.getCvv() != paymentDTO.getCvv() || paymentDTO.getAmount() == 0 || paymentDTO.getDescription().isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if (card == null) {
            return new ResponseEntity<>("Card doesn??t exists", HttpStatus.FORBIDDEN);
        }
        LocalDate dateNow = LocalDate.now();
        LocalDate thruDate = card.getThruDate();

        if (thruDate.isBefore(dateNow)) {
            return new ResponseEntity<>("Expired card", HttpStatus.FORBIDDEN);
        }

        if (accountPayment == null) {
            return new ResponseEntity<>("Insufficient balance", HttpStatus.FORBIDDEN);
        }

        Account accountWithFunds = accountPayment.stream().findFirst().get();
        Double balance = accountWithFunds.getBalance();
        Double newBalance = balance - paymentDTO.getAmount();

        Transaction debitTransaction = new Transaction(TransactionType.DEBIT, paymentDTO.getAmount(), paymentDTO.getDescription(), dateNow, accountWithFunds);
        transactionRepository.save(debitTransaction);
        accountWithFunds.setBalance(newBalance);
        return new ResponseEntity<>("Transaction created!", HttpStatus.CREATED);
    }


}
