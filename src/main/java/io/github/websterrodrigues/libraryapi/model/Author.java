package io.github.websterrodrigues.libraryapi.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity //Mapeamento JPA
@Table(name = "tb_autor")
@EntityListeners(AuditingEntityListener.class)//Observa todas as operações e observa se as annotations @CreatedDate e @LastModifiedDate existem
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome", nullable = false, length = 100)
    private String name;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "nacionalidade", nullable = false, length = 50)
    private String nationality;


    @CreatedDate //Toda vez que for persistir ele coloca a data atual automaticamente
    @Column(name = "data_cadastro")
    private LocalDateTime registrationDate;

    @LastModifiedDate
    @Column(name = "data_atualizacao")
    private LocalDateTime updateDate;

    @Column(name = "id_usuario")
    private UUID userId;


    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    @Transient
    private List<Book> books = new ArrayList<>();


    public Author(){

    }

    public Author(UUID id, String name, String nationality, LocalDate dateOfBirth) {
        this.id = id;
        this.name = name;
        this.nationality = nationality;
        this.dateOfBirth = dateOfBirth;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public List<Book> getBooks() {
        return books;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(id, author.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", nationality='" + nationality + '\'' +
                '}';
    }
}
