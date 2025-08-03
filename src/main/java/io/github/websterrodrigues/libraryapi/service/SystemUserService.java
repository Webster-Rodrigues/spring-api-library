package io.github.websterrodrigues.libraryapi.service;

import io.github.websterrodrigues.libraryapi.exceptions.EntityNotFoundException;
import io.github.websterrodrigues.libraryapi.model.Book;
import io.github.websterrodrigues.libraryapi.model.SystemUser;
import io.github.websterrodrigues.libraryapi.repository.SystemUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class SystemUserService {

    @Autowired
    private SystemUserRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    //Utilizando o Bean PasswordEncoder para criptografar a senha do usuário
    public void save(SystemUser systemUser){
        systemUser.setPassword(encoder.encode(systemUser.getPassword()));
        repository.save(systemUser);
    }

    public SystemUser findById(UUID id) {
        Optional<SystemUser> obj = repository.findById(id);
        return obj.orElseThrow(()-> new EntityNotFoundException(String.format("Usuário não encontrado! ID: %s", id)));
    }

    public void delete(UUID id){
        SystemUser systemUser = findById(id);
        repository.delete(systemUser);
    }

    public SystemUser findByLogin(String login){
        Optional<SystemUser> obj = repository.findByLogin(login);
        return obj.orElseThrow(() -> new EntityNotFoundException(String.format("Usuário não encontrado! Login: %s", login)));
    }

    public SystemUser findByEmail(String email){
        Optional<SystemUser> obj = repository.findByEmail(email);
        return obj.orElseThrow(() -> new EntityNotFoundException(String.format("Usuário não encontrado! Email: %s", email)));
    }

}
