package io.github.websterrodrigues.libraryapi.controller;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

public interface GenericController {

    default URI generateHeaderLocation(UUID id){
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}

//ServletUriComponentsBuilder: Cria uma URI; fromCurrentRequest: Pega os dados da requisição atual; buildAndExpand: Sinaliza qual parâmetro séra colocado no path
