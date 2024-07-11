package com.Challenge.LiterAlura.model;

public enum Idiomas {

    ESP("es"), ENG("en"), FRA("fr"), POR("pt"),JPN("ja");

    private final String idioma;

    Idiomas(String idioma) {
        this.idioma = idioma;
    }

    public String getIdioma() {

        return idioma;
    }

    public static Idiomas fromString(String text) {

        for (Idiomas idioma : Idiomas.values()) {
            if (idioma.getIdioma().equalsIgnoreCase(text)) {
                return idioma;
            }
        }
        throw new IllegalArgumentException("No se reconoce el idioma: " + text);
    }
}
