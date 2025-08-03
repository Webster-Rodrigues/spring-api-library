package io.github.websterrodrigues.libraryapi.validator;

import io.github.websterrodrigues.libraryapi.exceptions.DuplicateRecordException;
import io.github.websterrodrigues.libraryapi.model.Client;
import io.github.websterrodrigues.libraryapi.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ClientValidator {

    @Autowired
    private ClientRepository repository;

    public void validade(Client client){
        if(isDuplicateClient(client)){
            throw new DuplicateRecordException("Cliente já cadastrado!");
        }
    }

    private boolean isDuplicateClient(Client client){
        return repository.findByClientId(client.getClientId()).isPresent();
    }

}
