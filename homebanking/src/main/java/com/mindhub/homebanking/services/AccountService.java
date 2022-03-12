package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;


public interface AccountService {

    public AccountDTO findById(Long id);
}
