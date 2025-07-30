package io.github.websterrodrigues.libraryapi.controller;

import io.github.websterrodrigues.libraryapi.dto.AuthorDTO;
import io.github.websterrodrigues.libraryapi.dto.mappers.AuthorMapper;
import io.github.websterrodrigues.libraryapi.model.Author;
import io.github.websterrodrigues.libraryapi.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/authors")
public class AuthorController implements GenericController {

    @Autowired
    private AuthorService service;

    @Autowired
    private AuthorMapper mapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Permite que apenas usuários com a role ADMIN acessem este endpoint
    public ResponseEntity<Void> save(@RequestBody @Valid AuthorDTO dto) {

        Author author = mapper.toEntity(dto);
        service.save(author);
        URI location = generateHeaderLocation(author.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<AuthorDTO> getDetails(@PathVariable String id) {
        var idAuthor = UUID.fromString(id);

        AuthorDTO dto = mapper.toDto(service.findById(idAuthor));
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id) {

        service.delete(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<AuthorDTO>> findByFilter(
            @RequestParam(value = "nome", required = false) String name,
            @RequestParam(value = "nacionalidade", required = false) String nationality) {

        List<Author> list = service.searchByExample(name, nationality);
        List<AuthorDTO> listDTO = list.stream().map(mapper::toDto).toList();
        return ResponseEntity.ok(listDTO);
    }

    @PutMapping({"{id}"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody @Valid AuthorDTO dto) {
        try {
            UUID idAuthor = UUID.fromString(id);
            Author author = mapper.toEntity(dto);
            author.setId(idAuthor); //Garante que o ID do autor seja o mesmo do parâmetro da URL
            service.update(author);
            return ResponseEntity.noContent().build();
        }
        catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }

    }
}

