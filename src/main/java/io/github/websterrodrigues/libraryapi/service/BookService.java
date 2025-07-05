package io.github.websterrodrigues.libraryapi.service;

import io.github.websterrodrigues.libraryapi.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

    @Autowired
    private BookRepository repository;



}
