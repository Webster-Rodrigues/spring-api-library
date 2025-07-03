package io.github.websterrodrigues.libraryapi.service;

import io.github.websterrodrigues.libraryapi.model.Author;
import io.github.websterrodrigues.libraryapi.repository.AuthorRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository repository;

    public Author save(Author author){
        return repository.save(author);
    }

    public Optional<Author> getDetails(UUID id){
        return repository.findById(id);
    }
    public Author findById(UUID id) {
        Optional<Author> obj = repository.findById(id);
        return obj.orElseThrow(()-> new ObjectNotFoundException("Autor não encontrado", id));
    }

    public void delete(UUID id){
        Author author =  repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Autor não encontrado", id));
        try {
            repository.delete(author);
        }
        catch (DataIntegrityViolationException ex){
            throw new DataIntegrityViolationException("Autor não pode ser excluído, pois possui livros associados.", ex);
        }
        catch (Exception e){
            throw new RuntimeException("Erro ao excluir autor: " + e.getMessage(), e);
        }

    }





}
