package io.github.websterrodrigues.libraryapi.service;

import io.github.websterrodrigues.libraryapi.model.Author;
import io.github.websterrodrigues.libraryapi.repository.AuthorRepository;
import io.github.websterrodrigues.libraryapi.validator.AuthorValidator;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Author save(Author author){
        validator.validate(author);
        return repository.save(author);
    }

    public void update(Author author){
        repository.findById(author.getId());
        validator.validate(author);
        repository.save(author);
    }

    public Optional<Author> getDetails(UUID id){
        return repository.findById(id);
    }

    public Author findById(UUID id) {
        Optional<Author> obj = repository.findById(id);
        return obj.orElseThrow(()-> new ObjectNotFoundException("Autor não encontrado ", id));
    }

    public void delete(UUID id){
        Author author = findById(id);
        repository.delete(author);
    }

    public List<Author> findByFilter(String name, String nationality) {
        if (name != null && nationality != null) {
            return repository.findByNameAndNationality(name, nationality);
        }

        if(name != null) {
            return repository.findByName(name);
        }

        if (nationality != null){
            return repository.findByNationality(nationality);
        }
        return repository.findAll();
    }


}
