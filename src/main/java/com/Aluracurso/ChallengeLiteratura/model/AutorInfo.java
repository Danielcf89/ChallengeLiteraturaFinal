package com.Aluracurso.ChallengeLiteratura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "authors")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer birthYear;
    private Integer deathYear;

    @ManyToOne
    @JoinColumn(name = "book_id") // Nombre de la columna que referencia al libro
    private Book book;

    // Constructor vacío
    public Autor() {}

    // Constructor copia
    public Autor(Autor autor) {
        this.name = autor.name;
        this.birthYear = autor.birthYear;
        this.deathYear = autor.deathYear;
        this.book = autor.book;
    }

    // Constructor con argumentos
    public Autor(String name, Integer birthYear, Integer deathYear, Book book) {
        this.name = name;
        this.birthYear = birthYear;
        this.deathYear = deathYear;
        this.book = book;
    }

    // Getters y Setters
    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Integer getDeathYear() {
        return deathYear;
    }

    public void setDeathYear(Integer deathYear) {
        this.deathYear = deathYear;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Autor{" +
                "name='" + name + '\'' +
                ", birthYear=" + birthYear +
                ", deathYear=" + deathYear +
                '}';
    }
}
