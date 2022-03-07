package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;


import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/clients")
    public List<ClientDTO> getClients() {
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());
    }

    @GetMapping("clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {
        ClientDTO client = new ClientDTO(clientRepository.findById(id).orElse(null));
        return client;
    }

    @GetMapping("/clients/current")
        public ClientDTO getCurrentClientDTO(Authentication authentication) {
            ClientDTO client = new ClientDTO(clientRepository.findByEmail(authentication.getName()));
            return client;
    }

    int min = 11111111;
    int max = 99999999;
    public int getRandomNumber(int max, int min) {
        return (int) ((Math.random() * ( max - min)) + min);
    }
    public String getStringRandomNumber(){
        int randomNumber = getRandomNumber(min, max);
        return String.valueOf(randomNumber);
    }

    @PostMapping("/clients")
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (clientRepository.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }

        String number =  getStringRandomNumber();
//        int nextInte = 9999999;
//        Random numberAccount = new Random();


        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        Account account = new Account("VIN " + number , LocalDateTime.now(),0.00,client, AccountType.SAVINGS,true);
        clientRepository.save(client);
        accountRepository.save(account);
        return new ResponseEntity<>("Client registered!",HttpStatus.CREATED);


    }



}





