package io.github.websterrodrigues.libraryapi.controller;

import io.github.websterrodrigues.libraryapi.dto.RecordBookDTO;
import io.github.websterrodrigues.libraryapi.exceptions.DuplicateRecordException;
import io.github.websterrodrigues.libraryapi.exceptions.ResponseError;
import io.github.websterrodrigues.libraryapi.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService service;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid RecordBookDTO book) {
        try {

            return ResponseEntity.ok(book);
        }
        catch (DuplicateRecordException ex){
            var errorMessage = ResponseError.conflictError(ex.getMessage());
            return ResponseEntity.status(errorMessage.Status()).body(errorMessage);
        }

    }










//    @GetMapping
//    @PostMapping
//    @DeleteMapping
}
