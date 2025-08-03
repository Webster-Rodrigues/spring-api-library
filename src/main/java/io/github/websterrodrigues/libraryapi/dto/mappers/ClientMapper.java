package io.github.websterrodrigues.libraryapi.dto.mappers;

import io.github.websterrodrigues.libraryapi.dto.ClientDTO;
import io.github.websterrodrigues.libraryapi.model.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    Client toEntity(ClientDTO dto);

    ClientDTO toDto(Client client);
}
