package io.github.websterrodrigues.libraryapi.controller;

import io.github.websterrodrigues.libraryapi.dto.SystemUserDTO;
import io.github.websterrodrigues.libraryapi.dto.mappers.SystemUserMapper;
import io.github.websterrodrigues.libraryapi.model.SystemUser;
import io.github.websterrodrigues.libraryapi.service.SystemUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("users")
@Tag(name = "Usuário")
public class SystemUserController implements  GenericController{

    @Autowired
    private SystemUserService service;

    @Autowired
    private SystemUserMapper mapper;


    @Operation(summary = "Salvar", description = "Cadastrar novo usuário.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado."),
            @ApiResponse(responseCode = "422", description = "Erro de validação."),
            @ApiResponse(responseCode = "409", description = "Usuário já cadastrado.")
    })
    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid SystemUserDTO dto){
        SystemUser systemUser = mapper.toEntity(dto);
        service.save(systemUser);
        URI location = generateHeaderLocation(systemUser.getId());
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Deletar", description = "Deletar usuário.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário deletado."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado."),
    })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){
        service.delete(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

}
