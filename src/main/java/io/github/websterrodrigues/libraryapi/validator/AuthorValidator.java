package io.github.websterrodrigues.libraryapi.validator;

import io.github.websterrodrigues.libraryapi.exceptions.DuplicateRecordException;
import io.github.websterrodrigues.libraryapi.model.Author;
import io.github.websterrodrigues.libraryapi.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthorValidator {

    @Autowired
    private AuthorRepository repository;

    public void validate(Author author){
        if(isDuplicateAuthor(author)){
            throw new DuplicateRecordException("Autor já cadastrado!");
        }
    }
    private boolean isDuplicateAuthor(Author author){
        Optional<Author> authorFound = repository.findByNameAndDateOfBirthAndNationality(
                author.getName(),
                author.getDateOfBirth(),
                author.getNationality()
        );

        if(author.getId() == null){
            return authorFound.isPresent();
        }
        return authorFound.map(obj -> !obj.getId().equals(author.getId())).orElse(false);
    }


}
