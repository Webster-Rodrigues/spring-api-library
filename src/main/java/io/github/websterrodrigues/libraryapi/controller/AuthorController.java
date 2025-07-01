package io.github.websterrodrigues.libraryapi.controller;

import io.github.websterrodrigues.libraryapi.dto.AuthorDTO;
import io.github.websterrodrigues.libraryapi.model.Author;
import io.github.websterrodrigues.libraryapi.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

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
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/ {id}").buildAndExpand(author.getId()).toUri();

        return ResponseEntity.created(location).build();

    }
}
