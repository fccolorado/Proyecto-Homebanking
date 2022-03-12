package com.mindhub.homebanking;


import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

    @Autowired
    private PasswordEncoder passwordEnconde;

    public static void main(String[] args) {
        SpringApplication.run(HomebankingApplication.class, args);
    }

    @Bean
    public CommandLineRunner initData(ClientRepository clientRepository,
                                      AccountRepository accountRepository,
                                      TransactionRepository transactionRepository,
                                      LoanRepository loanRepository,
                                      ClientLoanRepository clientLoanRepository,
                                      CardRepository cardRepository) {
        return (args) -> {

            Client melba = new Client("Melba", "Morel", "melba@gmail.com", passwordEnconde.encode("1234"));
            Client fermin = new Client("Fermin", "Colorado", "fermin.colorado@esumer.edu.co", passwordEnconde.encode("1234"));
            Client admin = new Client("Fermin", "Colorado", "fermin.colorado@mindhub.com", passwordEnconde.encode("1234"));

            clientRepository.save(melba);
            clientRepository.save(fermin);
            clientRepository.save(admin);


            Account vin001 = new Account("VIN001", LocalDateTime.now(), 5000.00, melba, AccountType.SAVINGS, true);
            Account vin002 = new Account("VIN002", LocalDateTime.now().plusDays(1), 7500.00, melba, AccountType.CURRENT, true);
            Account vin003 = new Account("VIN003", LocalDateTime.now(), 5000.00, fermin, AccountType.SAVINGS, true);
            Account vin004 = new Account("VIN004", LocalDateTime.now().plusDays(1), 7500.00, fermin, AccountType.SAVINGS, true);
            Account vin005 = new Account("VIN005", LocalDateTime.now(), 50000.00, melba, AccountType.CURRENT, true);

            accountRepository.save(vin001);
            accountRepository.save(vin002);
            accountRepository.save(vin003);
            accountRepository.save(vin004);
            accountRepository.save(vin005);

            LocalDate dateNow = LocalDate.now();
            LocalDate datePlusDays5 = dateNow.plusDays(5);
            LocalDate dateYesterday = dateNow.minusDays(5);

            Transaction transaction1 = new Transaction(TransactionType.DEBIT, 9400.00, "Payment PSE", datePlusDays5, vin001);
            Transaction transaction2 = new Transaction(TransactionType.CREDIT, 5400.00, "Bank deposit", dateNow, vin001);
            Transaction transaction3 = new Transaction(TransactionType.CREDIT, 2000.00, "Bank deposit", dateYesterday, vin001);
            Transaction transaction4 = new Transaction(TransactionType.DEBIT, 2000.00, "Payment PSE", dateYesterday, vin002);

            Transaction transaction5 = new Transaction(TransactionType.DEBIT, 2000.00, "Payment PSE", datePlusDays5, vin003);
            Transaction transaction6 = new Transaction(TransactionType.CREDIT, 8200.00, "Bank deposit", dateYesterday, vin003);
            Transaction transaction7 = new Transaction(TransactionType.DEBIT, 3700.00, "Payment  PSE", datePlusDays5, vin004);
            Transaction transaction8 = new Transaction(TransactionType.CREDIT, 1500.00, "Bank deposit", dateYesterday, vin004);
            Transaction transaction9 = new Transaction(TransactionType.CREDIT, 10000.00, "Payment PSE", dateYesterday, vin002);

            transactionRepository.save(transaction1);
            transactionRepository.save(transaction2);
            transactionRepository.save(transaction3);
            transactionRepository.save(transaction4);
            transactionRepository.save(transaction5);
            transactionRepository.save(transaction6);
            transactionRepository.save(transaction7);
            transactionRepository.save(transaction8);
            transactionRepository.save(transaction9);


            Loan mortgage = new Loan("Mortgage", 500000.0, Arrays.asList(12, 24, 36, 48, 60), 0.151);
            Loan personal = new Loan("Personal", 100000.0, Arrays.asList(6, 12, 24), 0.10);
            Loan automotive = new Loan("Automotive", 300000.0, Arrays.asList(6, 12, 24, 36), 0.20);


            loanRepository.save(mortgage);
            loanRepository.save(personal);
            loanRepository.save(automotive);


            ClientLoan creditoMelba1 = new ClientLoan(400000.0, 60, melba, mortgage);
            ClientLoan creditoMelba2 = new ClientLoan(50000.0, 12, melba, personal);
            ClientLoan creditoMelba3 = new ClientLoan(35000.0, 24, melba, automotive);

            clientLoanRepository.save(creditoMelba1);
            clientLoanRepository.save(creditoMelba2);

            LocalDate fromDate = LocalDate.now();
            LocalDate thruDate = fromDate.plusYears(5);


            Card card1 = new Card(melba.getFirstName() + " " + melba.getLastName(), CardType.DEBIT, CardColor.GOLD, "5456 5834 5674 8082", 345, fromDate, thruDate, melba, true);
            Card card2 = new Card(melba.getFirstName() + " " + melba.getLastName(), CardType.CREDIT, CardColor.PLATINUM, "5456 5834 5674 8081", 346, fromDate, thruDate, melba, true);
            Card card3 = new Card(melba.getFirstName() + " " + melba.getLastName(), CardType.CREDIT, CardColor.SILVER, "5456 5834 2035 8080", 346, fromDate, thruDate, melba, true);
            Card card4 = new Card(fermin.getFirstName() + " " + fermin.getLastName(), CardType.CREDIT, CardColor.SILVER, "5456 5834 5674 5456", 347, fromDate, fromDate, fermin, true);
            Card card5 = new Card(fermin.getFirstName() + " " + fermin.getLastName(), CardType.CREDIT, CardColor.SILVER, "5456 5834 5674 5555", 204, fromDate, thruDate, fermin, true);

            cardRepository.save(card1);
            cardRepository.save(card2);
            cardRepository.save(card3);
            cardRepository.save(card4);
            cardRepository.save(card5);
        };
    }
}