package com.comunired.reels.infrastructure.adapter.out.persistence;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "reels")
public class ReelDocument {

    @Id
    private String id;

    private String videoUrl;
    private String title;
    private String description;
    private String authorId;
    private String author;
    private String avatarUrl;
    private int likes;
    private int shares;
    private int vistas;
    private int comentariosCount;
    private boolean activo;
    private Instant fechaCreacion;

    @Indexed(expireAfterSeconds = 0)
    private Instant fechaExpiracion;

    private List<String> likedBy = new ArrayList<>();
    private List<String> savedBy = new ArrayList<>();
}
