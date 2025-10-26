package io.github.websterrodrigues.libraryapi.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private final static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //Captura o erro e retorna uma resposta personalizada
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY) //Mapeia o retorno do método
    public ResponseError handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        List<FieldError> fieldErrors = exception.getFieldErrors();
        List<ValidationError> listErros =  fieldErrors.stream().map(fe -> new ValidationError(fe.getField(), fe.getDefaultMessage())).toList();

        logger.error("[ERROR] Erro de validação: {}", listErros);

        return new ResponseError(HttpStatus.UNPROCESSABLE_ENTITY.value(), "Erro de validação.", listErros);
    }

    @ExceptionHandler(DuplicateRecordException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseError handleDuplicateRecordException(DuplicateRecordException exception){
        logger.error("[ERROR][TYPE] {} | Conflito encontrado: {}", exception.getClass().getSimpleName(), exception.getMessage());

        return ResponseError.conflictError(exception.getMessage());
    }

    @ExceptionHandler(InvalidOperationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseError handleInvalidOperationException(InvalidOperationException exception){
        logger.error("[ERROR][TYPE] {} | Operação inválida: {}", exception.getClass().getSimpleName(), exception.getMessage());

        return ResponseError.conflictError(exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleIllegalArgumentException(IllegalArgumentException exception){
        logger.error("[ERROR][TYPE] {} | Argumento ilegal: {}", exception.getClass().getSimpleName(), exception.getMessage());

        return ResponseError.responseError(exception.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError handleEntityNotFoundException(EntityNotFoundException exception){
        logger.error("[ERROR][TYPE] {} | Entidade não encontrada! {}", exception.getClass().getSimpleName(), exception.getMessage());

        return ResponseError.notFoundError(exception.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseError handleAccessDeniedException(AccessDeniedException exception){
        logger.error("[ERROR][TYPE] {} | Acesso negado. Usuário não possui permissão para acessar este recurso.", exception.getClass().getSimpleName());

        return new ResponseError(HttpStatus.FORBIDDEN.value(), "Acesso negado. Você não possui permissão para acessar este recurso.", List.of());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseError handleException(RuntimeException exception){

        logger.error("[ERROR] Erro inesperado - Tipo: {}, Mensagem: {}",
                exception.getClass().getName(),
                exception.getMessage());

        ValidationError validationError = ValidationError.fromException(exception);

        return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Ocorreu um erro inesperado.",
                List.of(validationError));
    }
}
