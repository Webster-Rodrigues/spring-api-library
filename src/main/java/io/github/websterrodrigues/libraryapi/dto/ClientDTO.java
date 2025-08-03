package io.github.websterrodrigues.libraryapi.dto;

import jakarta.validation.constraints.NotBlank;

public record ClientDTO(

        @NotBlank
        String clientId,

        @NotBlank
        String clientSecret,

        @NotBlank
        String redirectURI,

        @NotBlank
        String scope
) {
}
