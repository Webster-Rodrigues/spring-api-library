package io.github.websterrodrigues.libraryapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "Client")
public record ClientDTO(

        @NotBlank(message = "Campo obrigatório!")
        String clientId,

        @NotBlank(message = "Campo obrigatório!")
        String clientSecret,

        @NotBlank(message = "Campo obrigatório!")
        String redirectURI,

        @NotBlank(message = "Campo obrigatório!")
        String scope
) {
}
