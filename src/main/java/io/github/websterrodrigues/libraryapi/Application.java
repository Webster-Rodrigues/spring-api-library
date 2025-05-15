package io.github.websterrodrigues.libraryapi;

import io.github.websterrodrigues.libraryapi.model.Author;
import io.github.websterrodrigues.libraryapi.repository.AuthorRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		var context = SpringApplication.run(Application.class, args);
		AuthorRepository repository = context.getBean(AuthorRepository.class);
		examplesSaveRegister(repository);
	}

	public static void examplesSaveRegister(AuthorRepository repository){
		Author author = new Author();
		author.setName("AUTORTESTE");
		author.setNationality("BRASILEIRO");
		author.setDateOfBirth(LocalDate.of(1990, 1, 1));

		repository.save(author);

	}
}
