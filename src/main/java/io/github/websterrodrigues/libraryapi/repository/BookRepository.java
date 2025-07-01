package io.github.websterrodrigues.libraryapi.repository;

import io.github.websterrodrigues.libraryapi.model.Author;
import io.github.websterrodrigues.libraryapi.model.Book;
import io.github.websterrodrigues.libraryapi.model.enums.Genre;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * @see BookRepositoryTest
 */
public interface BookRepository extends JpaRepository<Book, UUID> {

    // Querry Methods
    List<Book> findByAuthor(Author author);
    List<Book> findByTitle(String title);

    //JPQL Referencia as entidades e as propriedades. Não referencia as tabelas e colunas
    @Query("SELECT b FROM Book as b ORDER BY b.title")
    List<Book> listAllBooksByTitle();

    @Query("SELECT a FROM Book b join b.author a ")
    List<Author> listAuthorsByBook();

    @Query("""
            SELECT b.genre FROM Book b JOIN b.author a
            WHERE a.nationality = 'BRASILEIRO' ORDER BY b.genre
            """)
    List<String> listGenresAuthorsBrazilians();

    //Demonstração de uso de @Query c/ parâmetros nomeados
    @Query("SELECT b FROM Book b WHERE b.genre = :genre") //Utiliza o : para referenciar o parâmetro
    List<Book> findByGenre(@Param("genre") Genre genre); //Utiliza o @Param para referenciar o parâmetro no método

    //Demonstração de uso de @Query c/ parâmetros posicionais
    @Query("SELECT b FROM Book b WHERE b.genre = ?1 order by ?2")
    List<Book> findByGenrePositionalParameters(Genre genre, String orderBy);

    @Modifying //Sinaliza que a operação vai modificar o banco de dados
    @Transactional //Abre uma transação para a operação
    @Query("DELETE FROM Book WHERE genre = ?1")
    void deleteByGenre(Genre genre);

    @Modifying
    @Transactional
    @Query("UPDATE Book set publicationDate = ?1 WHERE id = ?2")
    void updateDate(LocalDate date, UUID id);


}
