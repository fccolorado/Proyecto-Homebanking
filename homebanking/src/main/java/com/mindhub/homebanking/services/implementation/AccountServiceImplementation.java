package com.mindhub.homebanking.services.implementation;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;



@Service
public class AccountServiceImplementation implements AccountService {
    @Autowired
    AccountRepository accountRepository;

    @Override
    public AccountDTO findById(Long id) {
        return null;
    }


//    @Override
//    public AccountDTO getAccount(Long id) {
//        List<AccountDTO> accountList = accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
//        List<AccountDTO> accountListTrue = accountList.stream().filter(accountDTO ->
//                accountDTO.isAccountStatus()
//        ).collect(toList());
//        return accountListTrue;
//    }
}
