package com.Challenge.LiterAlura.model;

import java.util.stream.Collectors;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String titulo;
    @Enumerated(EnumType.STRING)
    private Idiomas idioma;
    private String copyright;
    private Integer descargas;
    @ManyToOne
    private Autor autor;

    public Libro() {
    }

    public Libro(DatoLibros libros) {
        this.id = libros.id();
        this.titulo = libros.titulo();
        this.idioma = Idiomas.fromString(libros.idiomas().stream().limit(1).collect(Collectors.joining()));
        this.copyright = libros.copyright();
        this.descargas = libros.descargas();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Idiomas getIdioma() {
        return idioma;
    }

    public void setIdioma(Idiomas idioma) {
        this.idioma = idioma;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public Integer getDescargas() {
        return descargas;
    }

    public void setDescargas(Integer descargas) {
        this.descargas = descargas;
    }

    @Override
    public String toString() {
        return """
               ------------ Libro ------------
               id=""" + id
                + ", titulo='" + titulo + '\''
                + ", idioma=" + idioma
                + ", copyright='" + copyright + '\''
                + ", descargas=" + descargas
                + ", autor=" + autor
                + "\n-----------------------------------\n";
    }

}
