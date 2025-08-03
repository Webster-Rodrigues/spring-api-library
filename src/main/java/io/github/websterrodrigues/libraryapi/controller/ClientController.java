package io.github.websterrodrigues.libraryapi.controller;

import io.github.websterrodrigues.libraryapi.dto.ClientDTO;
import io.github.websterrodrigues.libraryapi.dto.mappers.ClientMapper;
import io.github.websterrodrigues.libraryapi.model.Client;
import io.github.websterrodrigues.libraryapi.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("clients")
public class ClientController implements GenericController {

    @Autowired
    private ClientService service;

    @Autowired
    private ClientMapper mapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> save(@RequestBody @Valid ClientDTO dto){
        Client client = mapper.toEntity(dto);
        service.save(client);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



}
