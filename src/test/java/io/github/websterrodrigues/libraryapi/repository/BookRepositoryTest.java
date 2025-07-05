package io.github.websterrodrigues.libraryapi.repository;

import io.github.websterrodrigues.libraryapi.model.Author;
import io.github.websterrodrigues.libraryapi.model.Book;
import io.github.websterrodrigues.libraryapi.model.enums.Genre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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
        book.setTitle("Test Bookficação");
        book.setGenre(Genre.FICCAO);
        book.setPublicationDate(LocalDate.of(2002, 1, 1));

        Author author = authorRepository.findById(UUID.fromString("5fb24c5c-5ac9-483e-a2d1-82ea441eef71")).orElse(null);
        //Estado do autor no banco é Transient
        //authorRepository.save(author);

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

    @Test
    void findByTitleTest() {
        List<Book> list = repository.findByTitle("Test Book Cascade");
        list.forEach(System.out::println);

    }

    @Test
    void listBooksWithQuerryJPQL(){
        var result = repository.listAllBooksByTitle();
        result.forEach(System.out::println);
    }

    @Test
    void listAuthorsByBookTest(){
        var result= repository.listAuthorsByBook();
        result.forEach(System.out::println);
    }

    @Test
    void listGenresAuthorsBraziliansTest(){
        var result = repository.listGenresAuthorsBrazilians();
        result.forEach(System.out::println);
    }

    @Test
    void listByGenreParamTest(){
        var result = repository.findByGenre(Genre.FANTASIA);
        result.forEach(System.out::println);
    }

    @Test
    void listByGenreParamPositionalParametersTest(){
        var result = repository.findByGenrePositionalParameters(Genre.FANTASIA, "title");
        result.forEach(System.out::println);
    }

    @Test
    void deleteByGenreTest(){
        repository.deleteByGenre(Genre.FICCAO);
    }

    @Test
    void updateDateTeste(){
        repository.updateDate(LocalDate.of(2000,10, 1),UUID.fromString("1e21313b-b812-4df7-9232-eb5e88609b06"));
    }

}
