package com.comunired.shared.infrastructure.rest;

import com.comunired.usuarios.domain.repository.UsuariosRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/api/reniec")
public class ReniecController {

    private static final Logger log = LoggerFactory.getLogger(ReniecController.class);

    private final UsuariosRepository usuariosRepository;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Value("${apisperu.token:}")
    private String apisperuToken;

    @Value("${APIPERU_DEV_TOKEN:}")
    private String apiperuDevToken;

    public ReniecController(UsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(8))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    @GetMapping("/{dni}")
    public ResponseEntity<?> consultar(@PathVariable String dni) {
        if (dni == null || !dni.matches("\\d{8}")) {
            return ResponseEntity.badRequest().body(Map.of("error", "DNI debe tener 8 dígitos"));
        }

        var existente = usuariosRepository.findByDni(dni);
        if (existente != null) {
            return ResponseEntity.ok(Map.of(
                "encontrado", true,
                "yaRegistrado", true,
                "nombres", existente.getNombre(),
                "apellidoPaterno", existente.getApellido(),
                "mensaje", "Este DNI ya está registrado"
            ));
        }

        // Intento 1: apisperu.com
        Map<String, Object> apisperuResult = consultarApisperu(dni);
        if (apisperuResult != null) return ResponseEntity.ok(apisperuResult);

        // Intento 2: apiperu.dev
        Map<String, Object> apiperuResult = consultarApiperuDev(dni);
        if (apiperuResult != null) return ResponseEntity.ok(apiperuResult);

        return ResponseEntity.ok(Map.of(
            "encontrado", false,
            "yaRegistrado", false,
            "mensaje", "DNI no encontrado en RENIEC"
        ));
    }

    private Map<String, Object> consultarApisperu(String dni) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://dniruc.apisperu.com/api/v1/dni/" + dni + "?token=" + apisperuToken))
                    .header("Accept", "application/json")
                    .timeout(Duration.ofSeconds(8))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            log.info("apisperu.com respondió con status {} para DNI {}", response.statusCode(), dni);

            if (response.statusCode() == 200) {
                String body = response.body();
                @SuppressWarnings("unchecked")
                Map<String, Object> data = objectMapper.readValue(body, Map.class);

                if (Boolean.FALSE.equals(data.get("success"))) {
                    log.warn("apisperu.com: {} para DNI {}", data.get("message"), dni);
                    return null;
                }

                String nombres = (String) data.getOrDefault("nombres", "");
                if (!nombres.isEmpty()) {
                    return Map.of(
                        "encontrado", true,
                        "yaRegistrado", false,
                        "nombres", nombres,
                        "apellidoPaterno", data.getOrDefault("apellidoPaterno", ""),
                        "apellidoMaterno", data.getOrDefault("apellidoMaterno", "")
                    );
                }
            }
        } catch (Exception e) {
            log.warn("apisperu.com falló para DNI {}: {}", dni, e.getMessage());
        }
        return null;
    }

    private Map<String, Object> consultarApiperuDev(String dni) {
        String[] tokens = {apisperuToken, apiperuDevToken};
        for (String token : tokens) {
            if (token == null || token.isBlank()) continue;
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://apiperu.dev/api/dni/" + dni + "?api_token=" + token))
                        .header("Accept", "application/json")
                        .timeout(Duration.ofSeconds(8))
                        .GET()
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                log.info("apiperu.dev respondió con status {} para DNI {}", response.statusCode(), dni);

                if (response.statusCode() == 200) {
                    String body = response.body();
                    @SuppressWarnings("unchecked")
                    Map<String, Object> root = objectMapper.readValue(body, Map.class);

                    if (Boolean.TRUE.equals(root.get("success"))) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> data = (Map<String, Object>) root.get("data");
                        if (data != null) {
                            String nombres = (String) data.getOrDefault("nombres", "");
                            if (!nombres.isEmpty()) {
                                return Map.of(
                                    "encontrado", true,
                                    "yaRegistrado", false,
                                    "nombres", nombres,
                                    "apellidoPaterno", data.getOrDefault("apellido_paterno", ""),
                                    "apellidoMaterno", data.getOrDefault("apellido_materno", "")
                                );
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.warn("apiperu.dev falló para DNI {}: {}", dni, e.getMessage());
            }
        }
        return null;
    }
}
