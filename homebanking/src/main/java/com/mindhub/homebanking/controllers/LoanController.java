package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ClientLoanRepository clientLoanRepository;
    @Autowired
    TransactionRepository transactionRepository;


    @GetMapping("/loans")
    public List<LoanDTO> getLoans() {
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(toList());
    }

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<String> createLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication) {
        Client currentClient = clientRepository.findByEmail(authentication.getName());
        Account account = accountRepository.findByNumber(loanApplicationDTO.getDestinationAccountNumber());
        Loan loan = loanRepository.findByName(loanApplicationDTO.getName());


        if (loanApplicationDTO == null) {
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO.getName().isEmpty() || loanApplicationDTO.getAmount() == 0 || loanApplicationDTO.getPayments() == 0 || loanApplicationDTO.getName().isEmpty() )
            return new ResponseEntity<>("Missing data1", HttpStatus.FORBIDDEN);
        ;

        if (account == null) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (!currentClient.getAccounts().contains(account)) {
            return new ResponseEntity<>("The account doesn't exist", HttpStatus.FORBIDDEN);

        }
        if (loan == null) {
            return new ResponseEntity<>("The loan doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAmount() > loan.getMaxAmount()) {
            return new ResponseEntity<>("The maximum loan amount is exceeded", HttpStatus.FORBIDDEN);//
        }

        LocalDate dateNow = LocalDate.now();
        String description = loanApplicationDTO.getName() + " loan approved";

        //SUMO EL MONTO DEL PRÉSTAMO A  LA CUENTA DESTINO
        Double creditAmount = account.getBalance() + loanApplicationDTO.getAmount();

        //SUMO EL INTERÉS DEL 20% AL MONTO TOTAL DEL PRÉSTAMO
//        Double tax = (loanApplicationDTO.getAmount() * loan.getPercentage()) + loanApplicationDTO.getAmount();

        ClientLoan clientLoan = new ClientLoan(loanApplicationDTO.getAmount(), loanApplicationDTO.getPayments(), currentClient, loan);

        Transaction creditTransaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), description, dateNow, account);

        //ACTUALIZO EL SALDO DE LA CUENTA + DESEMBOLSO DEL PRÉSTAMO
        account.setBalance(creditAmount);

        transactionRepository.save(creditTransaction);
        clientLoanRepository.save(clientLoan);

        return new ResponseEntity<>("Loan created!", HttpStatus.CREATED);
    }


     @PostMapping("/create/loans")
    public ResponseEntity<String> createLoan(@RequestBody LoanDTO loanDTO){

        if (loanDTO.getName().isEmpty() || loanDTO.getMaxAmount() == 0 || loanDTO.getPayments().isEmpty() || loanDTO.getPercentage() == 0 )
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        ;
         Loan newLoan = new Loan(loanDTO.getName(), loanDTO.getMaxAmount(), loanDTO.getPayments(),loanDTO.getPercentage());
         loanRepository.save(newLoan);
         return new ResponseEntity<>("Loan created!", HttpStatus.CREATED);
    }


}
