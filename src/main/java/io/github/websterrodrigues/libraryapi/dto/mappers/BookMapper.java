package io.github.websterrodrigues.libraryapi.dto.mappers;

import io.github.websterrodrigues.libraryapi.dto.RecordBookDTO;
import io.github.websterrodrigues.libraryapi.model.Book;
import io.github.websterrodrigues.libraryapi.repository.AuthorRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class BookMapper {

    @Autowired
    AuthorRepository authorRepository;

    @Mapping(target = "author", expression = "java(authorRepository.findById(dto.authorId()).orElse(null))")
    public abstract Book toEntity(RecordBookDTO dto);


}
