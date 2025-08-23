package io.github.websterrodrigues.libraryapi.dto;

import io.github.websterrodrigues.libraryapi.model.enums.Genre;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.ISBN;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Schema(name = "LivroCadastro")
public record RecordBookDTO (
        @ISBN
        @NotBlank(message = "Campo obrigatório!")
        String isbn,

        @NotBlank(message = "Campo obrigatório!")
        @Size(min = 3, max = 150, message = "Campo não corresponde os padrões de tamanho!")
        String title,

        @NotNull(message = "Campo obrigatório!")
        @Past(message = "Data de publicação deve ser anterior a data atual!")
        LocalDate publicationDate,

        Genre genre,
        BigDecimal price,

        @NotNull(message = "Campo obrigatório!")
        UUID authorId){
}
