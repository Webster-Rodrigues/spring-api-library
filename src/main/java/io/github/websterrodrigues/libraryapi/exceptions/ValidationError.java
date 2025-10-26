package io.github.websterrodrigues.libraryapi.exceptions;

public record ValidationError(String field, String message) {

    public static ValidationError fromException(RuntimeException ex) {
        return new ValidationError(
                ex.getClass().getSimpleName(),
                ex.getMessage() != null ? ex.getMessage() : "Sem mensagem disponível."
        );
    }
}

