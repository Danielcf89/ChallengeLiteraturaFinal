package com.Aluracurso.ChallengeLiteratura.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class ConsumoAPI {

    /**
     * Método principal para obtener datos desde una API REST.
     *
     * @param url URL de la API a consumir.
     * @return El cuerpo de la respuesta como un String.
     */
    public String obtenerDatos(String url) {
        // Validar que la URL no sea nula o vacía
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("La URL no puede ser nula o vacía.");
        }

        // Crear cliente y solicitud HTTP
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            // Enviar solicitud y recibir respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Validar código de estado HTTP
            if (response.statusCode() != 200) {
                throw new RuntimeException("Estado HTTP inesperado: " + response.statusCode());
            }

            return response.body();

        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt(); // Restaurar estado de interrupción del hilo
            throw new RuntimeException("Error al realizar la solicitud HTTP: " + e.getMessage(), e);
        }
    }
}
