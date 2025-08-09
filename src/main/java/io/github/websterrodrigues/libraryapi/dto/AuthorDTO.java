package io.github.websterrodrigues.libraryapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

//Record classe imutável
public record AuthorDTO(
        UUID id,

        @NotBlank(message = "Campo obrigatório!") //Barra a strng vazia e nula
        @Size(min = 3, max = 100, message = "Campo não corresponde os padrões de tamanho!")
        String name,

        @NotNull(message = "Campo obrigatório!")
        @Past(message = "Data de nascimento deve ser anterior a data atual!")
        LocalDate dateOfBirth,

        @NotBlank(message = "Campo obrigatório!")
        @Size(min = 2, max = 50, message = "Campo não corresponde os padrões de tamanho!")
        String nationality) {

}
