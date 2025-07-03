package io.github.websterrodrigues.libraryapi.controller;

import io.github.websterrodrigues.libraryapi.dto.AuthorDTO;
import io.github.websterrodrigues.libraryapi.model.Author;
import io.github.websterrodrigues.libraryapi.service.AuthorService;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    public AuthorService service;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody AuthorDTO authorDTO){
        Author author = authorDTO.mapedAuthor();
        service.save(author);

        //ServletUriComponentsBuilder: Cria uma URI; fromCurrentRequest: Pega os dados da requisição atual; buildAndExpand: Sinaliza qual parâmetro séra colocado no path
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(author.getId()).toUri();

        return ResponseEntity.created(location).build();

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
    public ResponseEntity<Void> delete(@PathVariable String id){
        try{
            service.delete(UUID.fromString(id));
            return ResponseEntity.noContent().build();
        }
        catch (ObjectNotFoundException e){
            return ResponseEntity.notFound().build();
        }

    }
}
