package com.Challenge.LiterAlura.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Challenge.LiterAlura.model.Autor;
import com.Challenge.LiterAlura.model.Idiomas;
import com.Challenge.LiterAlura.model.Libro;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    @Query("SELECT a FROM Libro l JOIN l.autor a WHERE a.nombre LIkE %:nombre%")
    Optional<Autor> buscarAutorPorNombre(@Param("nombre") String nombre);

    @Query("SELECT l FROM Libro l JOIN l.autor a WHERE l.titulo LIKE %:nombre%")
    Optional<Libro> buscarLibroPorNombre(@Param("nombre") String nombre);

    @Query("SELECT a FROM Autor a WHERE a.fallecimiento > :fecha")
    List<Autor> buscarAutoresVivos(@Param("fecha") Integer fecha);

    @Query("SELECT l FROM Autor a JOIN a.libros l WHERE l.idioma = :idioma")
    List<Libro> buscarLibrosPorIdioma(@Param("idioma") Idiomas idioma);

    @Query("SELECT l FROM Autor a JOIN a.libros l ORDER BY l.descargas DESC LIMIT 10")
    List<Libro> top10Libros();

    @Query("SELECT a FROM Autor a WHERE a.nacimiento = :fecha")
    List<Autor> listarAutoresNacimiento(@Param("fecha") Integer fecha);

    @Query("SELECT a FROM Autor a WHERE a.fallecimiento = :fecha")
    List<Autor> listarAutoresFallecimiento(@Param("fecha") Integer fecha);

    @Query("SELECT l FROM Autor a JOIN a.libros l")
    List<Libro> buscarTodosLosLibros();

}
