package com.Aluracurso.ChallengeLiteratura;

import com.Aluracurso.ChallengeLiteratura.model.*;
import com.Aluracurso.ChallengeLiteratura.repositorio.LibroRepositorio;
import com.Aluracurso.ChallengeLiteratura.service.ClienteAPI;
import com.Aluracurso.ChallengeLiteratura.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
public class ChallengeLiteraturaApplication implements CommandLineRunner {

	private final LibroRepositorio repositorio;
	private final ClienteAPI clienteAPI;
	private final ConvierteDatos convierteDatos;
	private final Scanner scanner = new Scanner(System.in);
	private static final String BASE_URL = "https://gutendex.com/books/";

	@Autowired
	public ChallengeLiteraturaApplication(LibroRepositorio repositorio, ClienteAPI clienteAPI, ConvierteDatos convierteDatos) {
		this.repositorio = repositorio;
		this.clienteAPI = clienteAPI;
		this.convierteDatos = convierteDatos;
	}

	public static void main(String[] args) {
		SpringApplication.run(ChallengeLiteraturaApplication.class, args);
	}

	@Override
	public void run(String... args) {
		mostrarMenu();
	}

	public void mostrarMenu() {
		int opcion = -1;
		while (opcion != 0) {
			System.out.println("""
                    Menú:
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    0 - Salir
                    """);
			System.out.print("Seleccione una opción: ");
			opcion = scanner.nextInt();
			scanner.nextLine(); // Consumir la nueva línea

			switch (opcion) {
				case 1 -> buscarYGuardarLibro();
				case 2 -> mostrarLibrosRegistrados();
				case 3 -> listarAutoresRegistrados();
				case 4 -> obtenerAutoresVivosPorAnio();
				case 5 -> buscarLibrosPorIdioma();
				case 0 -> System.out.println("Saliendo del programa...");
				default -> System.out.println("Opción no válida. Inténtelo de nuevo.");
			}
		}
	}

	private void buscarYGuardarLibro() {
		System.out.print("Introduzca el título del libro: ");
		String titulo = scanner.nextLine();

		try {
			// Obtener datos de la API
			String json = clienteAPI.obtenerDatos("?search=" + titulo.replace(" ", "+"));
			Datos datos = convierteDatos.obtenerDatos(json, Datos.class);

			// Validar resultados
			if (datos == null || datos.resultados() == null || datos.resultados().isEmpty()) {
				System.out.println("No se encontraron libros con el título especificado.");
				return;
			}

			// Buscar libro
			Optional<LibroInfo> libroEncontrado = datos.resultados().stream()
					.filter(libro -> libro.titulo().toLowerCase().contains(titulo.toLowerCase()))
					.findFirst();

			if (libroEncontrado.isPresent()) {
				LibroInfo libroInfo = libroEncontrado.get();
				Libro libro = new Libro(
						libroInfo.titulo(),
						libroInfo.lenguajes(),
						libroInfo.descargas(),
						new ArrayList<>()
				);

				for (Autor autor : libroInfo.autores()) {
					AutorInfo autorInfo = new AutorInfo(
							autor.name(),
							autor.birthYear(),
							autor.deathYear(),
							libro
					);
					libro.getAutores().add(autorInfo);
				}

				repositorio.save(libro);
				System.out.println("Libro guardado en la base de datos: " + libro);
			} else {
				System.out.println("No se encontró un libro con el título exacto.");
			}
		} catch (Exception e) {
			System.err.println("Error al buscar o guardar el libro: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void mostrarLibrosRegistrados() {
		List<Libro> libros = repositorio.findAll();
		if (libros.isEmpty()) {
			System.out.println("No hay libros registrados.");
		} else {
			libros.forEach(System.out::println);
		}
	}

	private void listarAutoresRegistrados() {
		List<AutorInfo> autores = repositorio.obtenerAutoresInfo();
		if (autores.isEmpty()) {
			System.out.println("No hay autores registrados.");
		} else {
			autores.forEach(System.out::println);
		}
	}

	private void obtenerAutoresVivosPorAnio() {
		System.out.print("Introduzca el año: ");
		int anio = scanner.nextInt();
		scanner.nextLine(); // Consumir nueva línea

		List<AutorInfo> autores = repositorio.obtenerAutoresVivosDespuesDe(anio);
		if (autores.isEmpty()) {
			System.out.println("No se encontraron autores vivos después del año " + anio);
		} else {
			autores.forEach(System.out::println);
		}
	}

	private void buscarLibrosPorIdioma() {
		System.out.print("Introduzca el idioma (EN, ES, FR, IT, PT): ");
		String idioma = scanner.nextLine().trim();
		try {
			Lenguaje lenguaje = Lenguaje.fromString(idioma.toLowerCase());
			List<Libro> libros = repositorio.findByLenguaje(lenguaje);
			if (libros.isEmpty()) {
				System.out.println("No se encontraron libros en el idioma " + idioma);
			} else {
				libros.forEach(System.out::println);
			}
		} catch (IllegalArgumentException e) {
			System.out.println("Idioma no válido.");
		}
	}
}

