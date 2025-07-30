package io.github.websterrodrigues.libraryapi.controller;

import io.github.websterrodrigues.libraryapi.dto.RecordBookDTO;
import io.github.websterrodrigues.libraryapi.dto.SearchBookDTO;
import io.github.websterrodrigues.libraryapi.dto.mappers.BookMapper;
import io.github.websterrodrigues.libraryapi.model.Book;
import io.github.websterrodrigues.libraryapi.model.enums.Genre;
import io.github.websterrodrigues.libraryapi.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/books")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')") // Permite que apenas usuários com as roles USER ou ADMIN acessem os endpoints deste controller
public class BookController implements GenericController {

    @Autowired
    private BookService service;

    @Autowired
    private BookMapper mapper;

    @PostMapping
    public ResponseEntity<Void> insert(@RequestBody @Valid RecordBookDTO dto) {
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

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    //required = false indica que o parâmetro é opcional
    public ResponseEntity<Page<SearchBookDTO>> searchByFilter(
        @RequestParam (value = "isbn", required = false) String isbn,
        @RequestParam (value = "title", required = false) String title,
        @RequestParam (value = "author-name", required = false) String authorName,
        @RequestParam (value = "genre", required = false) Genre genre,
        @RequestParam (value = "year-publication", required = false) Integer yearDate,
        @RequestParam (value = "page", defaultValue = "0") Integer page,
        @RequestParam (value = "size", defaultValue = "10") Integer size) {

        Page<SearchBookDTO> list = service.searchByFilter(isbn, title, authorName, genre, yearDate, page, size).map(mapper::toDto);

        return ResponseEntity.ok(list);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody @Valid RecordBookDTO dto){
        try {
            UUID idBook = UUID.fromString(id);
            Book book = mapper.toEntity(dto);
            book.setId(idBook); //Garante que o ID do autor seja o mesmo do parâmetro da URL
            service.update(book);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }


//    @GetMapping
//    @PostMapping

}
