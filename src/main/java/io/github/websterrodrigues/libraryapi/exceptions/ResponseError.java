package io.github.websterrodrigues.libraryapi.exceptions;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ResponseError(int Status, String message, List<ValidationError> errors) {

    public static ResponseError responseError(String message){
        return new ResponseError(HttpStatus.BAD_REQUEST.value(), message, List.of());
    }

    public static ResponseError conflictError(String message){
        return new ResponseError(HttpStatus.CONFLICT.value(), message, List.of());
    }

    public static ResponseError notFoundError(String message){
        return new ResponseError(HttpStatus.NOT_FOUND.value(), message, List.of());
    }

}
