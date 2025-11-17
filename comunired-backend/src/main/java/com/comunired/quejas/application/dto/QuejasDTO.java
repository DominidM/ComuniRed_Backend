package com.comunired.quejas.application.dto;

import com.comunired.usuarios.application.dto.UsuariosDTO;
import com.comunired.categoria.application.dto.CategoriaDTO;
import com.comunired.estados_queja.application.dto.Estados_quejaDTO;
import com.comunired.evidencias.application.dto.EvidenciasDTO;
import com.comunired.comentarios.application.dto.ComentariosDTO;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class QuejasDTO {
    private String id;
    private String titulo;
    private String descripcion;
    private UsuariosDTO usuario;
    private CategoriaDTO categoria;
    private Estados_quejaDTO estado;
    private String ubicacion;
    private String imagen_url;
    private Instant fecha_creacion;
    private Instant fecha_actualizacion;
    private List<EvidenciasDTO> evidence;
    private VotesDTO votes;
    private ReactionsDTO reactions;
    private List<ComentariosDTO> comments;
    private Integer commentsCount;
    private Boolean canVote;
    private String userVote;

    public QuejasDTO() {}
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public UsuariosDTO getUsuario() { return usuario; }
    public void setUsuario(UsuariosDTO usuario) { this.usuario = usuario; }

    public CategoriaDTO getCategoria() { return categoria; }
    public void setCategoria(CategoriaDTO categoria) { this.categoria = categoria; }

    public Estados_quejaDTO getEstado() { return estado; }
    public void setEstado(Estados_quejaDTO estado) { this.estado = estado; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getImagen_url() { return imagen_url; }
    public void setImagen_url(String imagen_url) { this.imagen_url = imagen_url; }

    public Instant getFecha_creacion() { return fecha_creacion; }
    public void setFecha_creacion(Instant fecha_creacion) { this.fecha_creacion = fecha_creacion; }

    public Instant getFecha_actualizacion() { return fecha_actualizacion; }
    public void setFecha_actualizacion(Instant fecha_actualizacion) { this.fecha_actualizacion = fecha_actualizacion; }

    public List<EvidenciasDTO> getEvidence() { return evidence; }
    public void setEvidence(List<EvidenciasDTO> evidence) { this.evidence = evidence; }

    public VotesDTO getVotes() { return votes; }
    public void setVotes(VotesDTO votes) { this.votes = votes; }

    public ReactionsDTO getReactions() { return reactions; }
    public void setReactions(ReactionsDTO reactions) { this.reactions = reactions; }

    public List<ComentariosDTO> getComments() { return comments; }
    public void setComments(List<ComentariosDTO> comments) { this.comments = comments; }

    public Integer getCommentsCount() { return commentsCount; }
    public void setCommentsCount(Integer commentsCount) { this.commentsCount = commentsCount; }

    public Boolean getCanVote() { return canVote; }
    public void setCanVote(Boolean canVote) { this.canVote = canVote; }

    public String getUserVote() { return userVote; }
    public void setUserVote(String userVote) { this.userVote = userVote; }


    public static class VotesDTO {
        private Long yes;
        private Long no;
        private Long total;

        public VotesDTO() {}

        public Long getYes() { return yes; }
        public void setYes(Long yes) { this.yes = yes; }

        public Long getNo() { return no; }
        public void setNo(Long no) { this.no = no; }

        public Long getTotal() { return total; }
        public void setTotal(Long total) { this.total = total; }
    }


    public static class ReactionsDTO {
        private Map<String, Long> counts;
        private String userReaction;
        private Long total;

        public ReactionsDTO() {}

        public Map<String, Long> getCounts() { return counts; }
        public void setCounts(Map<String, Long> counts) { this.counts = counts; }

        public String getUserReaction() { return userReaction; }
        public void setUserReaction(String userReaction) { this.userReaction = userReaction; }

        public Long getTotal() { return total; }
        public void setTotal(Long total) { this.total = total; }
    }
}
