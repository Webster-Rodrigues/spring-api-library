package io.github.websterrodrigues.libraryapi.exceptions;

import org.hibernate.ObjectNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
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

    @ExceptionHandler(DuplicateRecordException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseError handleDuplicateRecordException(DuplicateRecordException exception){
        return ResponseError.conflictError(exception.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError handleEntityNotFoundException(EntityNotFoundException exception){
        return ResponseError.notFoundError(exception.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseError handleInvalidFieldException(InvalidFieldException exception){
        return new ResponseError(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Erro de validação.", List.of(new ValidationError(exception.getField(), exception.getMessage())));

    }


    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseError handleException(RuntimeException exception){
        return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ocorreu um erro inesperado. Etre em contato com o suporte.",
                List.of());
    }
}
