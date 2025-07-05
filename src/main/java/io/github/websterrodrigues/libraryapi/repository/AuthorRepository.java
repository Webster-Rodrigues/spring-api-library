package io.github.websterrodrigues.libraryapi.repository;

import io.github.websterrodrigues.libraryapi.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthorRepository extends JpaRepository<Author, UUID> {

    Optional<Author> findByNameAndDateOfBirthAndNationality(String name, LocalDate dateOfBirth, String nationality);

}
