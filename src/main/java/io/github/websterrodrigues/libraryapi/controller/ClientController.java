package io.github.websterrodrigues.libraryapi.controller;

import io.github.websterrodrigues.libraryapi.dto.ClientDTO;
import io.github.websterrodrigues.libraryapi.dto.mappers.ClientMapper;
import io.github.websterrodrigues.libraryapi.model.Client;
import io.github.websterrodrigues.libraryapi.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("clients")
@Tag(name = "Clients")
public class ClientController{

    @Autowired
    private ClientService service;

    @Autowired
    private ClientMapper mapper;

    @Operation(summary = "Salvar", description = "Cadastrar novo client.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Client cadastrado."),
            @ApiResponse(responseCode = "422", description = "Erro de validação."),
            @ApiResponse(responseCode = "409", description = "Client já cadastrado.")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> save(@RequestBody @Valid ClientDTO dto){
        Client client = mapper.toEntity(dto);
        service.save(client);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
