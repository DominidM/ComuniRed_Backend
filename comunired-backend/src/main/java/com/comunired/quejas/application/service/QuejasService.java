package com.comunired.quejas.application.service;

import com.comunired.quejas.domain.entity.Quejas;
import com.comunired.quejas.domain.repository.QuejasRepository;
import com.comunired.quejas.application.dto.QuejasDTO;
import com.comunired.quejas.application.dto.QuejasDTO.VotesDTO;
import com.comunired.quejas.application.dto.QuejasDTO.ReactionsDTO;
import com.comunired.usuarios.domain.entity.Usuario;
import com.comunired.usuarios.domain.repository.UsuariosRepository;
import com.comunired.usuarios.application.dto.UsuariosDTO;
import com.comunired.categoria.infrastructure.entity.Categoria;
import com.comunired.categoria.domain.ports.CategoriaRepository;
import com.comunired.categoria.application.dto.CategoriaDTO;
import com.comunired.estados_queja.domain.entity.Estados_queja;
import com.comunired.estados_queja.domain.repository.Estados_quejaRepository;
import com.comunired.estados_queja.application.dto.Estados_quejaDTO;
import com.comunired.evidencias.domain.repository.EvidenciasRepository;
import com.comunired.evidencias.application.dto.EvidenciasDTO;
import com.comunired.comentarios.domain.repository.ComentariosRepository;
import com.comunired.comentarios.application.dto.ComentariosDTO;
import com.comunired.reacciones.domain.repository.ReaccionesRepository;
import com.comunired.reacciones.domain.entity.Reacciones;
import com.comunired.tipos_reaccion.domain.repository.Tipos_reaccionRepository;
import com.comunired.usuarios.infrastructure.cloudinary.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuejasService {

    @Autowired
    private QuejasRepository quejasRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private Estados_quejaRepository estadosRepository;

    @Autowired
    private EvidenciasRepository evidenciasRepository;

    @Autowired
    private ComentariosRepository comentariosRepository;

    @Autowired
    private ReaccionesRepository reaccionesRepository;

    @Autowired
    private Tipos_reaccionRepository tiposReaccionRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    public List<QuejasDTO> findAll(String currentUserId) {
        List<Quejas> quejas = quejasRepository.findAll();
        return quejas.stream()
                .map(q -> toDTO(q, currentUserId))
                .collect(Collectors.toList());
    }

    public QuejasDTO findById(String id, String currentUserId) {
        Quejas queja = quejasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Queja no encontrada"));
        return toDTO(queja, currentUserId);
    }

    public List<QuejasDTO> findByUsuarioId(String usuarioId, String currentUserId) {
        List<Quejas> quejas = quejasRepository.findByUsuarioId(usuarioId);
        return quejas.stream()
                .map(q -> toDTO(q, currentUserId))
                .collect(Collectors.toList());
    }

    public QuejasDTO create(String titulo, String descripcion, String categoriaId, 
                        String ubicacion, String usuarioId) {
        
        Quejas queja = new Quejas();
        queja.setTitulo(titulo);
        queja.setDescripcion(descripcion);
        queja.setCategoria_id(categoriaId);
        queja.setUsuario_id(usuarioId);
        queja.setUbicacion(ubicacion);
        
        List<Estados_queja> estados = estadosRepository.listar();
        Estados_queja estadoPendiente = estados.stream()
                .filter(e -> "PENDIENTE".equalsIgnoreCase(e.getClave()))
                .findFirst()
                .orElse(null);
        
        if (estadoPendiente != null) {
            queja.setEstado_id(estadoPendiente.getId());
        }

        Quejas saved = quejasRepository.save(queja);
        return toDTO(saved, usuarioId);
    }


    public QuejasDTO update(String id, String titulo, String descripcion, String categoriaId, 
                        String estadoId, String ubicacion, String imagen_url) {
        Quejas queja = quejasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Queja no encontrada"));
        
        if (titulo != null) queja.setTitulo(titulo);
        if (descripcion != null) queja.setDescripcion(descripcion);
        if (categoriaId != null) queja.setCategoria_id(categoriaId);
        if (estadoId != null) queja.setEstado_id(estadoId);
        if (ubicacion != null) queja.setUbicacion(ubicacion);
        if (imagen_url != null) queja.setImagen_url(imagen_url);
        
        Quejas updated = quejasRepository.save(queja);
        return toDTO(updated, queja.getUsuario_id());
    }



    public boolean delete(String id) {
        if (!quejasRepository.existsById(id)) {
            return false;
        }
        quejasRepository.deleteById(id);
        return true;
    }

    private QuejasDTO toDTO(Quejas queja, String currentUserId) {
        QuejasDTO dto = new QuejasDTO();
        dto.setId(queja.getId());
        dto.setTitulo(queja.getTitulo());
        dto.setDescripcion(queja.getDescripcion());
        dto.setUbicacion(queja.getUbicacion());
        dto.setImagen_url(queja.getImagen_url());
        dto.setFecha_creacion(queja.getFecha_creacion());
        dto.setFecha_actualizacion(queja.getFecha_actualizacion());

        if (queja.getUsuario_id() != null) {
            Usuario usuario = usuariosRepository.findById(queja.getUsuario_id());
            if (usuario != null) {
                UsuariosDTO usuarioDTO = new UsuariosDTO();
                usuarioDTO.setId(usuario.getId());
                usuarioDTO.setNombre(usuario.getNombre());
                usuarioDTO.setApellido(usuario.getApellido());
                usuarioDTO.setFoto_perfil(usuario.getFoto_perfil());
                dto.setUsuario(usuarioDTO);
            }
        }

        if (queja.getCategoria_id() != null) {
            Optional<Categoria> categoriaOpt = categoriaRepository.buscarPorId(queja.getCategoria_id());
            if (categoriaOpt.isPresent()) {
                Categoria categoria = categoriaOpt.get();
                CategoriaDTO categoriaDTO = new CategoriaDTO();
                categoriaDTO.setId(categoria.getId());
                categoriaDTO.setNombre(categoria.getNombre());
                categoriaDTO.setDescripcion(categoria.getDescripcion());
                dto.setCategoria(categoriaDTO);
            }
        }

        if (queja.getEstado_id() != null) {
            Optional<Estados_queja> estadoOpt = estadosRepository.buscarPorId(queja.getEstado_id());
            if (estadoOpt.isPresent()) {
                Estados_queja estado = estadoOpt.get();
                Estados_quejaDTO estadoDTO = new Estados_quejaDTO();
                estadoDTO.setId(estado.getId());
                estadoDTO.setClave(estado.getClave());
                estadoDTO.setNombre(estado.getNombre());
                dto.setEstado(estadoDTO);
            }
        }

        List<EvidenciasDTO> evidencias = evidenciasRepository.findByQuejaId(queja.getId())
                .stream()
                .map(ev -> {
                    EvidenciasDTO evDTO = new EvidenciasDTO();
                    evDTO.setId(ev.getId());
                    evDTO.setUrl(ev.getUrl());
                    evDTO.setTipo(ev.getTipo());
                    return evDTO;
                })
                .collect(Collectors.toList());
        dto.setEvidence(evidencias);


        dto.setVotes(calculateVotes(queja.getId(), currentUserId));


        dto.setReactions(calculateReactions(queja.getId(), currentUserId));

        List<ComentariosDTO> comentarios = comentariosRepository.findByQuejaId(queja.getId())
                .stream()
                .map(com -> {
                    ComentariosDTO comDTO = new ComentariosDTO();
                    comDTO.setId(com.getId());
                    comDTO.setTexto(com.getTexto());
                    comDTO.setFecha_creacion(com.getFecha_creacion());
                    
                    if (com.getUsuario_id() != null) {
                        Usuario author = usuariosRepository.findById(com.getUsuario_id());
                        if (author != null) {
                            UsuariosDTO authorDTO = new UsuariosDTO();
                            authorDTO.setId(author.getId());
                            authorDTO.setNombre(author.getNombre());
                            authorDTO.setApellido(author.getApellido());
                            authorDTO.setFoto_perfil(author.getFoto_perfil());
                            comDTO.setAuthor(authorDTO);
                        }
                    }

                    
                    return comDTO;
                })
                .collect(Collectors.toList());
        dto.setComments(comentarios);
        dto.setCommentsCount(comentarios.size());

        dto.setCanVote(!hasUserVoted(queja.getId(), currentUserId));

        return dto;
    }

    private VotesDTO calculateVotes(String quejaId, String currentUserId) {
        VotesDTO votes = new VotesDTO();
        
        Optional<String> acceptIdOpt = tiposReaccionRepository.buscarPorKey("accept")
                .map(t -> t.getId());
        Optional<String> rejectIdOpt = tiposReaccionRepository.buscarPorKey("reject")
                .map(t -> t.getId());

        long yesCount = 0;
        long noCount = 0;

        if (acceptIdOpt.isPresent()) {
            yesCount = reaccionesRepository.countByQuejaIdAndTipoReaccionId(quejaId, acceptIdOpt.get());
        }
        if (rejectIdOpt.isPresent()) {
            noCount = reaccionesRepository.countByQuejaIdAndTipoReaccionId(quejaId, rejectIdOpt.get());
        }

        votes.setYes((long) yesCount);
        votes.setNo((long) noCount);
        votes.setTotal((long) (yesCount + noCount));

        return votes;
    }

    private ReactionsDTO calculateReactions(String quejaId, String currentUserId) {
        ReactionsDTO reactions = new ReactionsDTO();
        

        List<String> excludeKeys = Arrays.asList("accept", "reject");
        List<Reacciones> allReactions = reaccionesRepository.findByQuejaId(quejaId);
        
        Map<String, Long> counts = new HashMap<>();
        long total = 0;

        for (Reacciones reaccion : allReactions) {
            Optional<com.comunired.tipos_reaccion.domain.entity.Tipos_reaccion> tipoOpt = 
                tiposReaccionRepository.buscarPorId(reaccion.getTipo_reaccion_id());
            
            if (tipoOpt.isPresent()) {
                com.comunired.tipos_reaccion.domain.entity.Tipos_reaccion tipo = tipoOpt.get();
                if (!excludeKeys.contains(tipo.getKey())) {
                    String key = tipo.getKey();
                    counts.put(key, counts.getOrDefault(key, 0L) + 1);
                    
                    if (reaccion.getUsuario_id().equals(currentUserId)) {
                        reactions.setUserReaction(key);
                    }
                }
            }
        }

        total = counts.values().stream().mapToLong(Long::longValue).sum();
        
        reactions.setCounts(counts);
        reactions.setTotal((long) total);

        return reactions;
    }

    private boolean hasUserVoted(String quejaId, String currentUserId) {
        if (currentUserId == null) return false;
        
        Optional<String> acceptIdOpt = tiposReaccionRepository.buscarPorKey("accept")
                .map(t -> t.getId());
        Optional<String> rejectIdOpt = tiposReaccionRepository.buscarPorKey("reject")
                .map(t -> t.getId());

        if (acceptIdOpt.isPresent()) {
            Optional<Reacciones> acceptVote = reaccionesRepository
                    .findByQuejaIdAndUsuarioIdAndTipoReaccionId(quejaId, currentUserId, acceptIdOpt.get());
            if (acceptVote.isPresent()) return true;
        }

        if (rejectIdOpt.isPresent()) {
            Optional<Reacciones> rejectVote = reaccionesRepository
                    .findByQuejaIdAndUsuarioIdAndTipoReaccionId(quejaId, currentUserId, rejectIdOpt.get());
            if (rejectVote.isPresent()) return true;
        }

        return false;
    }
}
