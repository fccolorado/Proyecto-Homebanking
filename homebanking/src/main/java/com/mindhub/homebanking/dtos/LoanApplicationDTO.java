package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Loan;

import java.util.ArrayList;
import java.util.List;

public class LoanApplicationDTO {
    private String name;
    private Double amount;
    private int payments;
    private String destinationAccountNumber;

    public LoanApplicationDTO() {
    }

    public LoanApplicationDTO(String name, Double amount, int payments, String destinationAccountNumber) {
        this.name = name;
        this.amount = amount;
        this.payments = payments;
        this.destinationAccountNumber = destinationAccountNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getPayments() {
        return payments;
    }

    public void setPayments(int payments) {
        this.payments = payments;
    }

    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }

    public void setDestinationAccountNumber(String destinationAccountNumber) {
        this.destinationAccountNumber = destinationAccountNumber;
    }
}
