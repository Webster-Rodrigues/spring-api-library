package io.github.websterrodrigues.libraryapi.controller;

import io.github.websterrodrigues.libraryapi.dto.RecordBookDTO;
import io.github.websterrodrigues.libraryapi.dto.SearchBookDTO;
import io.github.websterrodrigues.libraryapi.dto.mappers.BookMapper;
import io.github.websterrodrigues.libraryapi.model.Book;
import io.github.websterrodrigues.libraryapi.model.enums.Genre;
import io.github.websterrodrigues.libraryapi.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Livros")
public class BookController implements GenericController {

    @Autowired
    private BookService service;

    @Autowired
    private BookMapper mapper;

    @Operation(summary = "Salvar", description = "Cadastrar novo livro.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Livro cadastrado."),
            @ApiResponse(responseCode = "422", description = "Erro de validação."),
            @ApiResponse(responseCode = "409", description = "Livro já cadastrado.")
    })
    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid RecordBookDTO dto) {
        Book book = mapper.toEntity(dto);
        service.save(book);
        URI url = generateHeaderLocation(book.getId());

        return ResponseEntity.created(url).build();
    }


    @Operation(summary = "Pesquisar ID", description = "Pesquisa um livro pelo id.")
    @ApiResponses({

            @ApiResponse(responseCode = "201", description = "Livro encontrado."),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado."),
            @ApiResponse(responseCode = "400", description = "Parâmetro inválido.")
    })
    @GetMapping("{id}")
    public ResponseEntity<SearchBookDTO> searchBook(@PathVariable @Valid String id) {

        SearchBookDTO book = mapper.toDto(service.findById(UUID.fromString(id)));
        return ResponseEntity.ok(book);
    }

    @Operation(summary = "Deletar", description = "Deleta um livro.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Livro deletado."),
            @ApiResponse(responseCode = "404", description = "Livro não encotnrado."),
            @ApiResponse(responseCode = "400", description = "Parâmetro inválido."),
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Pesquisar Filtros", description = "Pesquisa um livro por atributos.")
    @ApiResponses({

            @ApiResponse(responseCode = "201", description = "Livro encontrado."),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado."),
    })
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


    @Operation(summary = "Atualizar", description = "Atualiza um livro existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Livro atualizado."),
            @ApiResponse(responseCode = "409", description = "Livro já cadastrado."),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado.")
    })
    @PutMapping("{id}")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody @Valid RecordBookDTO dto){
        UUID idBook = UUID.fromString(id);
        Book book = mapper.toEntity(dto);
        book.setId(idBook);
        service.update(book);
        return ResponseEntity.noContent().build();
    }

}
