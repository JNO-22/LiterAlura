package com.Challenge.LiterAlura.principal;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import com.Challenge.LiterAlura.model.Autor;
import com.Challenge.LiterAlura.model.DatoLibros;
import com.Challenge.LiterAlura.model.Datos;
import com.Challenge.LiterAlura.model.Idiomas;
import com.Challenge.LiterAlura.model.Libro;
import com.Challenge.LiterAlura.repository.AutorRepository;
import com.Challenge.LiterAlura.service.ConsumoAPI;
import com.Challenge.LiterAlura.service.ConvierteDatos;

public class Principal {

    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private String URL_BASE = "https://gutendex.com/books/";
    private final AutorRepository repository;

    public Principal(AutorRepository repository) {
        this.repository = repository;
    }

    public void mostrarMenu() {
        var opciones = -1;
        var menu = """
            -------------------------
                  Menu Principal
            -------------------------
            1-Buscar Libros por Titulo
            2-Buscar Autores por Nombre
            3-Listar libros 
            4-Listar Autores
            5-Listar Autores Vivos
            6-Listar libros por idioma
            7-Listar autores por Año
            8-Top 10 libros mas descargados
            9-generar estadisticas
            -------------------------
            0-Salir
            -------------------------
            Elija una Opcion:
            """;

        while (opciones != 0) {
            System.out.println(menu);
            try {
                opciones = Integer.parseInt(teclado.nextLine());
                switch (opciones) {
                    case 1:
                        buscarLibroTitulo();
                        break;
                    case 2:
                        buscarAutorNombre();
                        break;
                    case 3:
                        listarLibrosRegistrados();
                        break;
                    case 4:
                        listarAutoresRegistrados();
                        break;
                    case 5:
                        listarAutoresVivos();
                        break;
                    case 6:
                        ListarLibrosIdioma();
                        break;
                    case 7:
                        listarAutoresPoraño();
                        break;
                    case 8:
                        top10();
                        break;
                    case 9:
                        estadisticas();
                        break;
                    case 0:
                        System.out.println("Saliendo...");
                        break;
                    default:
                        System.out.println("Opcion no valida");
                        break;
                }

            } catch (NumberFormatException e) {
                System.out.println("Opcion no valida" + e.getMessage());
            }
        }
    }

    public void buscarLibroTitulo() {
        System.out.println("Ingresa el Titulo del libro que deseas buscar: ");
        var nombre = teclado.nextLine();
        var json = consumoAPI.obtenerJson(URL_BASE + "?search=" + nombre.replace(" ", "+").toLowerCase());
        System.out.println(json);

        if (!json.isEmpty() || !json.contains("\"count\":0,\"next\":null,\"previous\":null,\"results\":[]")) {
            {
                var datos = conversor.obtenerDatos(json, Datos.class);
                Optional<DatoLibros> libro = datos.libros().stream().findFirst();

                if (libro.isPresent()) {
                    System.out.println(libro.get().toString());
                    try {
                        List<Libro> libroEncontrado = libro.stream().map(l -> new Libro(l)).collect(Collectors.toList());
                        Autor autorApi = libro.stream()
                                .flatMap(l -> l.autores().stream()
                                .map(a -> new Autor(a)))
                                .collect(Collectors.toList()).stream().findFirst().get();

                        Optional<Autor> autorDataBase = repository.buscarAutorPorNombre(libro.get().autores()
                                .stream().map(a -> a.nombre()).collect(Collectors.joining()));

                        Optional<Libro> librOptional = repository.buscarLibroPorNombre(nombre);
                        if (librOptional.isPresent()) {
                            System.out.println("Libro ya existente en la base de datos");
                        } else {
                            Autor autor;
                            if (autorDataBase.isPresent()) {
                                autor = autorDataBase.get();
                                System.out.println("Autor ya existente en la base de datos");
                            } else {
                                autor = autorApi;
                                repository.save(autor);
                            }
                            autor.setLibros(libroEncontrado);
                            repository.save(autor);
                        }
                    } catch (Exception e) {
                        System.out.println("Error en la base de datos: " + e.getMessage());
                    }
                }
            }

        } else {
            System.out.println("Libro no encontrado o no existente");
        }
    }

    public void listarLibrosRegistrados() {
        System.out.println("Libros registrados: " + repository.buscarTodosLosLibros().size());
        List<Libro> libros = repository.buscarTodosLosLibros();
        libros.forEach(libro -> System.out.println(libro.toString()));
    }

    public void ListarLibrosIdioma() {
        System.out.println("Ingresa el idioma del libro que deseas buscar: ");
        System.out.println("""
                -------------------------
                Seleccione:
                1-Ingles
                2-Espanol
                3-Frances
                4-Portugues
                5-Japones
                """);
        try {
            var idioma = Integer.parseInt(teclado.nextLine());
            switch (idioma) {
                case 1:
                    System.out.println("Libros en Ingles");
                    buscarLibroIdioma("ENG");
                    break;
                case 2:
                    System.out.println("Libros en Espanol");
                    buscarLibroIdioma("ESP");
                    break;
                case 3:
                    System.out.println("Libros en Frances");
                    buscarLibroIdioma("FRA");
                    break;
                case 4:
                    System.out.println("Libros en Portugues");
                    buscarLibroIdioma("POR");
                    break;
                case 5:
                    System.out.println("Libros en Japones");
                    buscarLibroIdioma("JPN");
                    break;
                default:
                    System.out.println("No se reconoce el idioma");
                    break;
            }

        } catch (Exception e) {
            System.out.println("Error en el formato de fecha");
        }
    }

    public void buscarLibroIdioma(String idioma) {
        try {
            Idiomas idiomas = Idiomas.valueOf(idioma.toUpperCase());
            List<Libro> libros = repository.buscarLibrosPorIdioma(idiomas);
            if (!libros.isEmpty()) {
                libros.forEach(System.out::println);
            } else {
                System.out.println("No se encontraron libros");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("No se reconoce el idioma");
        }
    }

    public void buscarAutorNombre() {
        System.out.println("Ingresa el Nombre del autor que deseas buscar: ");
        var nombre = teclado.nextLine();
        Optional<Autor> autor = repository.buscarAutorPorNombre(nombre);
        if (autor.isPresent()) {
            System.out.println(autor.get().toString());
        } else {
            System.out.println("Autor no encontrado");
        }
    }

    public void listarAutoresRegistrados() {
        System.out.println("Autores registrados: " + repository.findAll().size());
        List<Autor> autores = repository.findAll();
        autores.forEach(autor -> System.out.println(autor.toString()));
    }

    public void listarAutoresPoraño() {
        System.out.println("""
              -------------------------
              Seleccione la opcion
              -------------------------
              1 - Lista de autores por nacimiento
              2 - Lista de autores por fallecimiento
              """);

        try {
            var opcion = Integer.parseInt(teclado.nextLine());
            switch (opcion) {
                case 1:
                    System.out.println("Autores por nacimiento");
                    listarAutoresNacimiento();
                    break;
                case 2:
                    System.out.println("Autores por fallecimiento");
                    listarAutoresFallecimiento();
                    break;
                default:
                    System.out.println("No se reconoce la opcion");
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error en el formato de fecha");
        }
    }

    public void listarAutoresFallecimiento() {
        System.out.println("Ingresa el año de fallecimiento del autor que deseas buscar: ");
        try {
            var fecha = Integer.valueOf(teclado.nextLine());
            List<Autor> autores = repository.listarAutoresFallecimiento(fecha);
            if (!autores.isEmpty()) {
                autores.forEach(System.out::println);
            } else {
                System.out.println("No se encontraron autores");
            }
        } catch (Exception e) {
            System.out.println("Error en el formato de fecha");
        }
    }

    public void listarAutoresNacimiento() {
        System.out.println("Ingresa el año de nacimiento del autor que deseas buscar: ");
        try {
            var fecha = Integer.valueOf(teclado.nextLine());
            List<Autor> autores = repository.listarAutoresNacimiento(fecha);
            if (!autores.isEmpty()) {
                autores.forEach(System.out::println);
            } else {
                System.out.println("No se encontraron autores");
            }
        } catch (Exception e) {
            System.out.println("Error en el formato de fecha");
        }
    }

    public void listarAutoresVivos() {
        System.out.println("Ingresa el año para ver los autores vivos en esa fecha: ");
        try {
            var fecha = Integer.valueOf(teclado.nextLine());
            List<Autor> autores = repository.buscarAutoresVivos(fecha);
            if (!autores.isEmpty()) {
                autores.forEach(System.out::println);
            } else {
                System.out.println("No se encontraron autores");
            }
        } catch (Exception e) {
            System.out.println("Error en el formato de fecha");
        }
    }

    public void top10() {
        List<Libro> libros = repository.top10Libros();
        libros.forEach(System.out::println);
    }

    public void estadisticas() {
        System.out.println("Estadisticas obtenidas");
        var json = consumoAPI.obtenerJson(URL_BASE);
        var datos = conversor.obtenerDatos(json, Datos.class);
        IntSummaryStatistics est = datos.libros().stream()
                .filter(l -> l.descargas() > 0)
                .collect(Collectors.summarizingInt(DatoLibros::descargas));
        Integer media = (int) est.getAverage();
        System.out.println("""
                -------------------------
                Estadisticas de libros
                -------------------------
                """);
        System.out.println("Libros con descargas: " + est.getCount());
        System.out.println("Promedio de descargas: " + media);
        System.out.println("Maximo de descargas: " + est.getMax());
        System.out.println("Minimo de descargas: " + est.getMin());
    }
}
