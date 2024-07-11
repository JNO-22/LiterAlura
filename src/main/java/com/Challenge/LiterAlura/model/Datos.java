package com.Challenge.LiterAlura.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Datos(
        @JsonAlias("count")
        Integer total,
        @JsonAlias("results")
        List<DatoLibros> libros) {

}
