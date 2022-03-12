package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ClientService {

    public List<ClientDTO> getClients();
    public Client saveClient(Client client);
    public ClientDTO getClient(Long id);
    public ClientDTO findByEmail(String email);
    public Client findByEmailClient(String email);
    public ClientDTO getCurrentClientDTO(String email);
}
