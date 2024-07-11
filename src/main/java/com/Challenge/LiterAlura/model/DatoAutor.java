package com.Challenge.LiterAlura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatoAutor(
        @JsonAlias("name")
        String nombre,
        @JsonAlias("birth_year")
        Integer año_nacimiento,
        @JsonAlias("death_year")
        Integer año_muerte) {

}
