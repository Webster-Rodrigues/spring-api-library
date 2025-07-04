package io.github.websterrodrigues.libraryapi.dto;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ResponseException(int Status, String message, List<FieldException> errors) {

    public static ResponseException responseException(String message){
        return new ResponseException(HttpStatus.BAD_REQUEST.value(), message, List.of());
    }

    public static ResponseException conflictException(String message){
        return new ResponseException(HttpStatus.CONFLICT.value(), message, List.of());
    }
}
