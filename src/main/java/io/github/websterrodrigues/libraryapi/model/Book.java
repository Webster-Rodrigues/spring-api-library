package io.github.websterrodrigues.libraryapi.model;

import io.github.websterrodrigues.libraryapi.model.enums.Genre;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "livro")
@Data
public class Book {

    @Id
    @GeneratedValue(strategy =  GenerationType.UUID)
    private UUID id;

    private String isbn;

    @Column(name = "titulo", nullable = false, length = 150)
    private String title;

    @Column(name = "data_publicacao", nullable = false)
    private LocalDate publicationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "genero", nullable = false, length = 30)
    private Genre genre;

    @Column(name = "preco", precision = 18, scale = 2)
    private Double price;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Author author;
}
