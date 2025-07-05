package io.github.websterrodrigues.libraryapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;


@RestControllerAdvice
public class GlobalExceptionHandler {

    //Captura o erro e retorna uma resposta personalizada
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY) //Mapeia o retorno do método
    public ResponseError handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        List<FieldError> fieldErrors = exception.getFieldErrors();
        List<ValidationError> listErros =  fieldErrors.stream().map(fe -> new ValidationError(fe.getField(), fe.getDefaultMessage())).toList();

        return new ResponseError(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Erro de validação.", listErros);

    }
}
