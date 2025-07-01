package io.github.websterrodrigues.libraryapi;

import io.github.websterrodrigues.libraryapi.model.Author;
import io.github.websterrodrigues.libraryapi.repository.AuthorRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDate;

@SpringBootApplication
@EnableJpaAuditing //Habilita auditoria
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
