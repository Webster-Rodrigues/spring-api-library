package io.github.websterrodrigues.libraryapi.service;

import io.github.websterrodrigues.libraryapi.exceptions.EntityNotFoundException;
import io.github.websterrodrigues.libraryapi.model.Client;
import io.github.websterrodrigues.libraryapi.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    public Client save(Client client){
         String passwordEncoder = encoder.encode(client.getClientSecret());
         client.setClientSecret(passwordEncoder);
        return repository.save(client);
    }

    public Client findByClientId(String clientId) {
        return repository.findByClientId(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client não encontrado!  clientId: " + clientId));
    }

}
