package io.github.websterrodrigues.libraryapi.controller;

import io.github.websterrodrigues.libraryapi.dto.RecordBookDTO;
import io.github.websterrodrigues.libraryapi.dto.SearchBookDTO;
import io.github.websterrodrigues.libraryapi.dto.mappers.BookMapper;
import io.github.websterrodrigues.libraryapi.model.Book;
import io.github.websterrodrigues.libraryapi.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/books")
public class BookController implements GenericController {

    @Autowired
    private BookService service;

    @Autowired
    private BookMapper mapper;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid RecordBookDTO dto) {
        Book book = mapper.toEntity(dto);
        service.save(book);
        URI url = generateHeaderLocation(book.getId());

        return ResponseEntity.created(url).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<SearchBookDTO> searchBook(@PathVariable @Valid String id) {

        SearchBookDTO book = mapper.toDto(service.findById(UUID.fromString(id)));
        return ResponseEntity.ok(book);
    }


//    @GetMapping
//    @PostMapping
//    @DeleteMapping
}
