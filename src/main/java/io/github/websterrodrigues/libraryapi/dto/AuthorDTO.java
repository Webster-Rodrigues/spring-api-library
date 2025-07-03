package io.github.websterrodrigues.libraryapi.dto;

import io.github.websterrodrigues.libraryapi.model.Author;

import java.time.LocalDate;
import java.util.UUID;

//Record classe imutável
public record AuthorDTO(
        UUID id,
        String name,
        LocalDate dateOfBirth,
        String nationality) {

    public Author mapedAuthor(){
        Author author = new Author();
        author.setName(this.name);
        author.setDateOfBirth(this.dateOfBirth);
        author.setNationality(this.nationality);
        return author;
    }

}
