package io.github.websterrodrigues.libraryapi.dto;

import io.github.websterrodrigues.libraryapi.model.enums.Genre;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record SearchBookDTO(
        UUID id,
        String isbn,
        String title,
        LocalDate publicationDate,
        Genre genre,
        BigDecimal price,
        AuthorDTO author) {
}
