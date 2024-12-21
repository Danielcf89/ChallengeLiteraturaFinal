import com.Aluracurso.ChallengeLiteratura.model.Datos;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonMappingTest {
    package com.Aluracurso.ChallengeLiteratura;

import com.Aluracurso.ChallengeLiteratura.model.Datos;
import com.fasterxml.jackson.databind.ObjectMapper;

    public class JsonMappingTest {
        public static void main(String[] args) {
            String json = """
            {
                "count": 6,
                "next": null,
                "previous": null,
                "results": [
                    {
                        "id": 1342,
                        "title": "Pride and Prejudice",
                        "authors": [
                            {
                                "name": "Austen, Jane",
                                "birth_year": 1775,
                                "death_year": 1817
                            }
                        ],
                        "languages": ["en"],
                        "download_count": 56305
                    }
                ]
            }
        """;

            try {
                ObjectMapper mapper = new ObjectMapper();
                Datos datos = mapper.readValue(json, Datos.class);

                System.out.println("Cantidad de resultados: " + datos.resultados().size());
                datos.resultados().forEach(libro -> {
                    System.out.println("Título: " + libro.titulo());
                    System.out.println("Idiomas: " + libro.lenguajes());
                    System.out.println("Descargas: " + libro.descargas());
                    libro.autores().forEach(autor -> {
                        System.out.println("Autor: " + autor.name());
                        System.out.println("Año de nacimiento: " + autor.birthYear());
                        System.out.println("Año de muerte: " + autor.deathYear());
                    });
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
