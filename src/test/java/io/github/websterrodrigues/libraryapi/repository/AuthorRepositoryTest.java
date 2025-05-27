package io.github.websterrodrigues.libraryapi.repository;

import io.github.websterrodrigues.libraryapi.model.Author;
import io.github.websterrodrigues.libraryapi.model.Book;
import io.github.websterrodrigues.libraryapi.model.enums.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class AuthorRepositoryTest {

    @Autowired
    AuthorRepository repository;

    @Autowired
    BookRepository bookRepository;

    @Test
    public void saveTest(){
        Author author = new Author();
        author.setName("TESTEEE2");
        author.setNationality("ITALIANO");
        author.setDateOfBirth(LocalDate.of(1990, 1, 1));

        repository.save(author);
    }

    @Test
    public void updateTest(){
        var id = UUID.fromString("e19524b1-a8ff-46b1-8973-041922fd7b39");

        Optional<Author> author = repository.findById(id);

        if(author.isPresent()){
            System.out.println("Autor encontrado: " + author.get().getName());
            System.out.println("Data ANTIGA: " +author.get().getDateOfBirth());

            author.get().setDateOfBirth(LocalDate.of(2005, 1, 1));
            repository.save(author.get());
        }

    }

    @Test
    public void ListTest(){
        List<Author> list = repository.findAll();
        list.forEach(System.out::println);
    }

    @Test
    public void countAuthorsTest(){
        System.out.println("Total de autores: " +repository.count());
    }

    @Test
    public void deleteAuthorTest(){
        var id = UUID.fromString("e19524b1-a8ff-46b1-8973-041922fd7b39");

        Optional<Author> author = repository.findById(id);
        if(author.isPresent()){
            repository.delete(author.get());
        }
    }

    @Test
    public void deleteAuthorByIdTest(){
        var id = UUID.fromString("3e9fe1e5-93ac-4492-b606-e94dfdfee755");
        repository.deleteById(id);
    }

    @Test
    public void saveAuthorWithBooksTest() {
        Author author = new Author();
        author.setName("AUTOR TESTE3");
        author.setNationality("CANADENSE");
        author.setDateOfBirth(LocalDate.of(1990, 1, 1));

        Book book1 = new Book();
        book1.setIsbn("88888888");
        book1.setPrice(BigDecimal.valueOf(100));
        book1.setTitle("Test Book1");
        book1.setGenre(Genre.FICCAO);
        book1.setPublicationDate(LocalDate.of(2005, 8, 6));
        book1.setAuthor(author);

        Book book2 = new Book();
        book2.setIsbn("777777");
        book2.setPrice(BigDecimal.valueOf(100.99));
        book2.setTitle("Test Book2");
        book2.setGenre(Genre.ROMANCE);
        book2.setPublicationDate(LocalDate.of(2003, 8, 6));
        book2.setAuthor(author);

        Book book3 = new Book();
        book3.setIsbn("88888888");
        book3.setPrice(BigDecimal.valueOf(100));
        book3.setTitle("Test Book3");
        book3.setGenre(Genre.BIOGRAFIA);
        book3.setPublicationDate(LocalDate.of(2000, 8, 6));
        book3.setAuthor(author);

        author.getBooks().addAll(Arrays.asList(book1, book2, book3));
        repository.save(author);
        bookRepository.saveAll(author.getBooks());
    }

}
