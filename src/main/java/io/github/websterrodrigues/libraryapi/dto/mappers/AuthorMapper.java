package io.github.websterrodrigues.libraryapi.dto.mappers;

import ch.qos.logback.core.model.ComponentModel;
import io.github.websterrodrigues.libraryapi.dto.AuthorDTO;
import io.github.websterrodrigues.libraryapi.model.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") //Transforma a interface em um componente do Spring na hora da compilação
public interface AuthorMapper {

    Author toEntity(AuthorDTO dto);

    AuthorDTO toDto(Author author);

}
