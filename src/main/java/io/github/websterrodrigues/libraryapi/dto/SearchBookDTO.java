package io.github.websterrodrigues.libraryapi.dto;

import io.github.websterrodrigues.libraryapi.model.enums.Genre;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Schema(name = "LivroPesquisa")
public record SearchBookDTO(
        UUID id,
        String isbn,
        String title,
        LocalDate publicationDate,
        Genre genre,
        BigDecimal price,
        AuthorDTO author) {
}
