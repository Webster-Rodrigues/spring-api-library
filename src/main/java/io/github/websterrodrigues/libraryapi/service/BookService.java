package io.github.websterrodrigues.libraryapi.service;

import io.github.websterrodrigues.libraryapi.model.Book;
import io.github.websterrodrigues.libraryapi.repository.BookRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class BookService {

    @Autowired
    private BookRepository repository;


    public Book save(Book book) {
        return repository.save(book);
    }

    public Book findById(UUID id){
        Optional<Book> book = repository.findById(id);
        return book.orElseThrow(() -> new ObjectNotFoundException("Livro não encontrado: ", id));
    }


}
