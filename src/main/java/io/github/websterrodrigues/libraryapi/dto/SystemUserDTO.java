package io.github.websterrodrigues.libraryapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record SystemUserDTO(

        @NotBlank(message = "Campo obrigatório!")
        String login,

        @NotBlank(message = "Campo obrigatório!")
        String password,

        @NotBlank(message = "Campo obrigatório!")
        @Email(message = "E-mail inválido!")
        String email,

        @NotEmpty(message = "Declare ao menos uma role!")
        List<@NotBlank(message = "Verifique se não há campos vazios!") String> roles){

}
