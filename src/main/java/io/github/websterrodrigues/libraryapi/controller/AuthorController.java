package io.github.websterrodrigues.libraryapi.controller;

import io.github.websterrodrigues.libraryapi.dto.AuthorDTO;
import io.github.websterrodrigues.libraryapi.exceptions.ResponseError;
import io.github.websterrodrigues.libraryapi.exceptions.DuplicateRecordException;
import io.github.websterrodrigues.libraryapi.model.Author;
import io.github.websterrodrigues.libraryapi.service.AuthorService;
import jakarta.validation.Valid;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    public AuthorService service;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody @Valid AuthorDTO authorDTO){
        try {
            Author author = authorDTO.mapedAuthor();
            service.save(author);

            //ServletUriComponentsBuilder: Cria uma URI; fromCurrentRequest: Pega os dados da requisição atual; buildAndExpand: Sinaliza qual parâmetro séra colocado no path
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(author.getId()).toUri();

            return ResponseEntity.created(location).build();
        }
        catch (DuplicateRecordException ex){
            var errorMessage = ResponseError.conflictError(ex.getMessage());
            return ResponseEntity.status(errorMessage.Status()).body(errorMessage);
        }

    }

    @GetMapping("{id}")
    public ResponseEntity<AuthorDTO> getDetails(@PathVariable String id){
        var idAuthor = UUID.fromString(id);
        Optional<Author> authorOP = service.getDetails(idAuthor);
        if (authorOP.isPresent()){
            Author author = authorOP.get();
            AuthorDTO authorDTO = new AuthorDTO(
                    author.getId(),
                    author.getName(),
                    author.getDateOfBirth(),
                    author.getNationality());
            return ResponseEntity.ok(authorDTO);
        }
        return ResponseEntity.notFound().build();

    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> delete(@PathVariable String id){
        try{
            service.delete(UUID.fromString(id));
            return ResponseEntity.noContent().build();
        }
        catch (ObjectNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(ex.getMessage());
        }
        catch (DataIntegrityViolationException ex){
            var errorMessage = ResponseError.conflictError("Autor não pode ser excluído, pois possui livros associados.");
            return ResponseEntity.status(errorMessage.Status()).body(errorMessage);
        }
    }

    @GetMapping
    public ResponseEntity<List<AuthorDTO>> findByFilter(
            @RequestParam(value = "nome", required = false) String name,
            @RequestParam(value = "nacionalidade", required = false) String nationality){

        List<Author> list = service.searchByExample(name, nationality);
        List<AuthorDTO> listDTO = list.stream().map(author ->
                new AuthorDTO(
                author.getId(),
                author.getName(),
                author.getDateOfBirth(),
                author.getNationality())).toList();
        return ResponseEntity.ok(listDTO);
    }

    @PutMapping({"{id}"})
    public ResponseEntity<Object> update(@PathVariable String id, @RequestBody @Valid AuthorDTO dto){
        try{
            UUID idAuthor = UUID.fromString(id);
            var author = dto.mapedAuthor();
            author.setId(idAuthor); //Garante que o ID do autor seja o mesmo do parâmetro da URL
            service.update(author);
            return ResponseEntity.noContent().build();
        }
        catch (DuplicateRecordException ex){
            var errorMessage = ResponseError.conflictError(ex.getMessage());
            return ResponseEntity.status(errorMessage.Status()).body(errorMessage);
        }
        catch (ObjectNotFoundException ex){
            return ResponseEntity.notFound().build();
        }
        catch (IllegalArgumentException ex){
            return ResponseEntity.badRequest().build();
        }

    }
}

