package io.github.websterrodrigues.libraryapi.service;

import io.github.websterrodrigues.libraryapi.exceptions.EntityNotFoundException;
import io.github.websterrodrigues.libraryapi.model.Book;
import io.github.websterrodrigues.libraryapi.model.enums.Genre;
import io.github.websterrodrigues.libraryapi.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static io.github.websterrodrigues.libraryapi.repository.specs.BookSpecs.*;

@Service
public class BookService {

    @Autowired
    private BookRepository repository;


    public Book save(Book book) {
        return repository.save(book);
    }

    public Book findById(UUID id){
        Optional<Book> book = repository.findById(id);
        return book.orElseThrow(() -> new EntityNotFoundException(String.format("Livro não encontrado! ID: %s", id)));
    }

    public void delete(UUID id){
        Book book = findById(id);
        repository.delete(book);
    }

    public void update(Book book) {
        repository.findById(book.getId());
        repository.save(book);
    }

    public List<Book> searchByFilter(String isbn, String title, String authorName, Genre genre, Integer yearPublication) {

        //select * from book where 0=0
        Specification<Book> specs = Specification.where((root, query, cb) -> cb.conjunction());

        if (isbn != null) {
            specs = specs.and(isbnEquals(isbn));
        }

        if( title != null) {
            specs = specs.and(titleLike(title));
        }

        if (genre != null) {
            specs = specs.and(genreEquals(genre));
        }

        if(yearPublication != null) {
            specs = specs.and(yearPublicationEquals(yearPublication));
        }

        return repository.findAll(specs); //Encontra todos os livros que atendem as especificações criadas
    }

}
/**
 * A interface Specification permite construir consultas dinâmicas, reutilizáveis e encadeáveis usando critérios.

 * Exemplo:
 * Specification<Book> isbnSpec = (root, query, cb) -> cb.equal(root.get("isbn"), isbn);
 * // Gera: WHERE isbn = :isbn

 * - root: representa a entidade raiz da consulta (neste caso, Book). É usado para acessar os atributos da entidade.
 * - query: representa a consulta JPA em construção
 * - cb: fábrica de critérios, usada para criar expressões como equal, like, greaterThan, etc.

 * As Specifications podem ser combinadas usando métodos como .and(), .or(), permitindo composição de filtros dinâmicos.
 */
