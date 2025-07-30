package io.github.websterrodrigues.libraryapi.repository;

import io.github.websterrodrigues.libraryapi.model.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface SystemUserRepository extends JpaRepository<SystemUser, UUID> {
    Optional<SystemUser> findByLogin(String login);

}
