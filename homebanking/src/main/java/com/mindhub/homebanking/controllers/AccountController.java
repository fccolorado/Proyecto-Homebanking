package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import net.minidev.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

//    @GetMapping("/accounts")
//    public List<AccountDTO> getAccounts() {
//        return accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
//    }

    @GetMapping("/clients/current/accounts")
    public List<AccountDTO> getAccountsClientCurrent() {
        List<AccountDTO> accountList = accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
        List<AccountDTO> accountListTrue = accountList.stream().filter(accountDTO ->
           accountDTO.isAccountStatus()
        ).collect(toList());
        return accountListTrue;
    }


    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        AccountDTO account = new AccountDTO(accountRepository.findById(id).orElse(null));
        return account;
    }

    int min = 10000000;
    int max = 99999999;

    public int getRandomNumber(int max, int min) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public String getStringRandomNumber() {
        int randomNumber = getRandomNumber(min, max);
        return String.valueOf(randomNumber);
    }

    @PostMapping("clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication, @RequestParam AccountType type) {
        Client currentClient = clientRepository.findByEmail(authentication.getName());
        List<AccountDTO> accountList = currentClient.getAccounts().stream().map(AccountDTO::new).collect(toList());
        List<AccountDTO> accountListTrue = accountList.stream().filter(accountDTO ->
                accountDTO.isAccountStatus()
        ).collect(toList());

        if (accountListTrue.size() == 3) {
            return new ResponseEntity<>("Forbidden, you can only register 3 accounts", HttpStatus.FORBIDDEN);
        }

        String accountNumber = getStringRandomNumber();

        Account newAccount = new Account("VIN " + accountNumber, LocalDateTime.now(), 0.00, currentClient, type, true);
        accountRepository.save(newAccount);
        return new ResponseEntity<>("Created!", HttpStatus.CREATED);

    }


    @PatchMapping("/clients/current/accounts/{id}")
    public ResponseEntity<Object> deleteAccount(@PathVariable Long id) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account.getBalance() != 0) {
            return new ResponseEntity<>("El saldo debe estar en 0", HttpStatus.FORBIDDEN);
        }
        account.setAccountStatus(false);
        accountRepository.save(account);
        return new ResponseEntity<>("Account deleted!", HttpStatus.CREATED);
    }
}

