package com.comunired.historias.infrastructure.adapter.out.persistence;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "historias")
public class HistoriaDocument {

    @Id
    private String id;

    private String usuarioId;
    private String texto;
    private String imagenUrl;
    private String colorFondo;
    private int duracion;
    private boolean activa;
    private Instant fechaCreacion;

    @Indexed(expireAfterSeconds = 0)
    private Instant fechaExpiracion;  // TTL index — MongoDB borra automático

    private List<VistaDocument> vistas = new ArrayList<>();

    @Data
    public static class VistaDocument {
        private String usuarioId;
        private Instant fechaVista;
    }
}