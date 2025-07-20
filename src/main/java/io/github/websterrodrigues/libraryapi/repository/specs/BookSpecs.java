package io.github.websterrodrigues.libraryapi.repository.specs;

import io.github.websterrodrigues.libraryapi.model.Book;
import io.github.websterrodrigues.libraryapi.model.enums.Genre;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

//Armazena todas as especificações de Book
public class BookSpecs {

    public static Specification<Book> isbnEquals(String isbn){
        return ((root, query, cb) -> cb.equal(root.get("isbn"), isbn));
    }

    public static Specification<Book> titleLike(String title){
        return ((root, query, cb) -> cb.like(cb.upper(root.get("title")), "%" + title.toUpperCase() + "%" ));
    }

    public static Specification<Book> genreEquals(Genre genre){
        return ((root, query, cb) -> cb.equal(root.get("genre"), genre));
    }

    public static Specification<Book> yearPublicationEquals(Integer yearPublication){
        //and to_char(data_cadastro, 'YYYY') = yearPublication
        return ((root, query, cb) -> cb.equal(
                cb.function("to_char", String.class, root.get("publicationDate"), cb.literal("YYYY")), yearPublication.toString()));
    }

    public static Specification<Book> authorNameLike(String name) {
        return (root, query, cb) -> {
            Join<Object, Object> joinAuthor = root.join("author", JoinType.LEFT);

            return cb.like(cb.upper(joinAuthor.get("name")), "%" + name.toUpperCase() + "%");

            //return cb.like(cb.upper(root.get("author").get("name")), "%" + name.toUpperCase() + "%"); Solução sem join (Sempre será Inner Join)
        };
    }

    //Root.get -> Precisa colocar o nome do atributo
}
