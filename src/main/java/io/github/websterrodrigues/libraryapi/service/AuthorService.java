package io.github.websterrodrigues.libraryapi.service;

import io.github.websterrodrigues.libraryapi.exceptions.EntityNotFoundException;
import io.github.websterrodrigues.libraryapi.model.Author;
import io.github.websterrodrigues.libraryapi.model.SystemUser;
import io.github.websterrodrigues.libraryapi.repository.AuthorRepository;
import io.github.websterrodrigues.libraryapi.security.SecurityService;
import io.github.websterrodrigues.libraryapi.validator.AuthorValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository repository;

    @Autowired
    private AuthorValidator validator;

    @Autowired
    private SecurityService securityService;

    public Author save(Author author){
        validator.validate(author);
        SystemUser user = securityService.getAuthenticatedUser();
        author.setSystemUser(user);
        return repository.save(author);
    }

    public void update(Author author){
        repository.findById(author.getId());
        validator.validate(author);
        repository.save(author);
    }

    public Author findById(UUID id) {
        Optional<Author> obj = repository.findById(id);
        return obj.orElseThrow(()-> new EntityNotFoundException(String.format("Autor não encontrado! ID: %s", id)));
    }

    public void delete(UUID id){
        Author author = findById(id);
        repository.delete(author);
    }

    public List<Author> searchByExample(String name, String nationality){
        //Example trabalha com o exemplo de Author
        Author author = new Author();
        author.setName(name);
        author.setNationality(nationality);

        //ExampleMatcher permite configurar como o exemplo será comparado
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()//Ignora maiúsculas e minúsculas
                .withIgnoreNullValues()//Ignora valores nulos.
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);//Configura o exemplo para ser comparado como contendo.

        Example<Author> authorExample = Example.of(author, matcher);
        return repository.findAll(authorExample);
    }

}
