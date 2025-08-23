package io.github.websterrodrigues.libraryapi.validator;

import io.github.websterrodrigues.libraryapi.exceptions.DuplicateRecordException;
import io.github.websterrodrigues.libraryapi.exceptions.InvalidFieldException;
import io.github.websterrodrigues.libraryapi.model.Author;
import io.github.websterrodrigues.libraryapi.model.Book;
import io.github.websterrodrigues.libraryapi.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class BookValidator implements ValidateEntity<Book> {

    private static final int YEAR_MANDATORY_PRICE = 2020;

    @Autowired
    BookRepository repository;

    @Override
    public void validate(Book book){
        if(isDuplicateBook(book)){
            throw new DuplicateRecordException("ISBN já cadastrado!");
        }

        if (isMandatoryPriceNull(book)){
            throw new InvalidFieldException("price", "Livros com ano de publicação a partir de 2020, é obrigatório informar o preço!");
        }
    }

    private boolean isMandatoryPriceNull(Book book) {
        return book.getPrice() == null && book.getPublicationDate().getYear() >= YEAR_MANDATORY_PRICE;
    }

    private boolean isDuplicateBook(Book book){
        Optional<Book> bookFound = repository.findByIsbn(book.getIsbn());

        // Se o livro não tiver ID, significa que é um novo livro
        if(book.getId() == null){
            return bookFound.isPresent();
        }

        return bookFound.map(Book::getId).stream().anyMatch(id -> !id.equals(book.getId()));
        //Se o Optional<Book> tiver valor, pegue o ID do livro encontrado, ou seja, transforma Optional<Book> em um Optional<UUID>
        //Transforma o Optional<UUID> em um Stream<UUID> para usar o anyMatch que verifica se o ID do livro encontrado é diferente do ID do livro que está sendo validado.

    }


}
