package io.github.websterrodrigues.libraryapi.controller;

import io.github.websterrodrigues.libraryapi.dto.SystemUserDTO;
import io.github.websterrodrigues.libraryapi.dto.mappers.SystemUserMapper;
import io.github.websterrodrigues.libraryapi.model.SystemUser;
import io.github.websterrodrigues.libraryapi.service.SystemUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("users")
public class SystemUserController implements  GenericController{

    @Autowired
    private SystemUserService service;

    @Autowired
    private SystemUserMapper mapper;


    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid SystemUserDTO dto){
        SystemUser systemUser = mapper.toEntity(dto);
        service.save(systemUser);
        URI location = generateHeaderLocation(systemUser.getId());
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){
        service.delete(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

//    @GetMapping
//    public ResponseEntity<SystemUser> serchByLogin(@RequestParam(required = true) String login ){
//        SystemUser byLogin = service.findByLogin(login);
//        return ResponseEntity.ok(byLogin);
//    }







}
