package io.github.websterrodrigues.libraryapi.service;

import io.github.websterrodrigues.libraryapi.exceptions.EntityNotFoundException;
import io.github.websterrodrigues.libraryapi.model.Book;
import io.github.websterrodrigues.libraryapi.model.SystemUser;
import io.github.websterrodrigues.libraryapi.model.enums.Genre;
import io.github.websterrodrigues.libraryapi.repository.BookRepository;
import io.github.websterrodrigues.libraryapi.security.SecurityService;
import io.github.websterrodrigues.libraryapi.validator.BookValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import static io.github.websterrodrigues.libraryapi.repository.specs.BookSpecs.*;

@Service
public class BookService {

    @Autowired
    private BookRepository repository;

    @Autowired
    private BookValidator validator;

    @Autowired
    private SecurityService securityService;

    @Lazy
    @Autowired
    private BookService self;


    public Book save(Book book) {
        validator.validate(book);
        SystemUser user = securityService.getAuthenticatedUser();
        book.setSystemUser(user);
        return repository.save(book);
    }

    public Book findById(UUID id){
        Optional<Book> obj = repository.findById(id);
        return obj.orElseThrow(() -> new EntityNotFoundException(String.format("Livro não encontrado! ID: %s", id)));
    }

    public void delete(UUID id){
        Book book = self.findById(id);
        repository.delete(book);
    }

    public void update(Book book) {
        self.findById(book.getId());
        SystemUser user = securityService.getAuthenticatedUser();
        book.setSystemUser(user);
        validator.validate(book);
        repository.save(book);
    }

    public Page<Book> searchByFilter(String isbn, String title, String authorName, Genre genre,
                                     Integer yearPublication, Integer page, Integer size) {

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

        Pageable pageResquest = PageRequest.of(page, size);

        return repository.findAll(specs, pageResquest); //Encontra todos os livros que atendem as especificações criadas
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
