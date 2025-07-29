package io.github.websterrodrigues.libraryapi.dto.mappers;

import io.github.websterrodrigues.libraryapi.dto.SystemUserDTO;
import io.github.websterrodrigues.libraryapi.model.SystemUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SystemUserMapper {

    SystemUser toEntity(SystemUserDTO dto);

    SystemUserDTO toDto(SystemUser systemUser);

}
