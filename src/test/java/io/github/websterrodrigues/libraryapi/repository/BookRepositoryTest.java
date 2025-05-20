package io.github.websterrodrigues.libraryapi.repository;

import io.github.websterrodrigues.libraryapi.model.Author;
import io.github.websterrodrigues.libraryapi.model.Book;
import io.github.websterrodrigues.libraryapi.model.enums.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@SpringBootTest
public class BookRepositoryTest {

    @Autowired
    BookRepository repository;

    @Autowired
    AuthorRepository authorRepository; //Busca autor no banco para associar ao livro

    @Test
    public void saveTestNoCascade(){
        Book book = new Book();
        book.setIsbn("123456789");
        book.setPrice(BigDecimal.valueOf(100));
        book.setTitle("Test Book");
        book.setGenre(Genre.FANTASIA);
        book.setPublicationDate(LocalDate.of(2002, 1, 1));

        Author author = authorRepository.findById(UUID.fromString("36f2e4c1-86b1-4b75-b859-540d221dbd43")).orElse(null);
        //Estado do autor no banco é Transient
        authorRepository.save(author);

        book.setAuthor(author);

        repository.save(book);
    }
    @Test
    public void saveTest(){
        Book book = new Book();
        book.setIsbn("999999999");
        book.setPrice(BigDecimal.valueOf(100));
        book.setTitle("Test Book Cascade");
        book.setGenre(Genre.FANTASIA);
        book.setPublicationDate(LocalDate.of(2002, 1, 1));

        Author author = new Author();
        author.setName("MARIA");
        author.setNationality("BRBRBRBRBBR");
        author.setDateOfBirth(LocalDate.of(1990, 1, 1));

        book.setAuthor(author);
        //Autor salvo no modo cascade

        repository.save(book);
    }

    @Test
    void updateTest(){
        UUID bookId = UUID.fromString("18044394-f036-47ea-858e-a8bdc6ab0717");
        var book = repository.findById(bookId).orElse(null);

        UUID authorId = UUID.fromString("883cff14-e561-4e97-8168-6e14ea054de4");
        var author = authorRepository.findById(authorId).orElse(null);

        book.setAuthor(author);
        repository.save(book);
    }

    @Test
    void delteTest(){
        UUID bookId = UUID.fromString("18044394-f036-47ea-858e-a8bdc6ab0717");
        repository.deleteById(bookId);
    }


}
