package com.comunired.quejas.application.service;

import com.comunired.quejas.domain.entity.Quejas;
import com.comunired.quejas.domain.repository.QuejasRepository;
import com.comunired.quejas.application.dto.QuejasDTO;
import com.comunired.quejas.application.dto.QuejasDTO.VotesDTO;
import com.comunired.quejas.application.dto.QuejasPageDTO;
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
import com.comunired.historial_evento.application.service.HistorialEventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    private HistorialEventoService historialEventoService;

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

        // ✅ Estado inicial: VOTACION (el usuario publica y entra directamente a votación)
        List<Estados_queja> estados = estadosRepository.listar();
        Estados_queja estadoVotacion = estados.stream()
                .filter(e -> "VOTACION".equalsIgnoreCase(e.getClave()))
                .findFirst()
                .orElse(null);

        if (estadoVotacion != null) {
            queja.setEstado_id(estadoVotacion.getId());
        }

        Quejas saved = quejasRepository.save(queja);

        // Historial: registra el estado real inicial
        historialEventoService.registrar(
                saved.getId(),
                usuarioId,
                "creada",
                null,
                "VOTACION",
                "Reporte publicado y en espera de votos de la comunidad"
        );

        return toDTO(saved, usuarioId);
    }

    public QuejasDTO update(String id, String titulo, String descripcion, String categoriaId,
            String estadoId, String ubicacion, String imagen_url) {
        Quejas queja = quejasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Queja no encontrada"));

        if (titulo != null) {
            queja.setTitulo(titulo);
        }
        if (descripcion != null) {
            queja.setDescripcion(descripcion);
        }
        if (categoriaId != null) {
            queja.setCategoria_id(categoriaId);
        }
        if (estadoId != null) {
            queja.setEstado_id(estadoId);
        }
        if (ubicacion != null) {
            queja.setUbicacion(ubicacion);
        }
        if (imagen_url != null) {
            queja.setImagen_url(imagen_url);
        }

        Quejas updated = quejasRepository.save(queja);
        return toDTO(updated, queja.getUsuario_id());
    }

    public QuejasDTO clasificarRiesgo(String quejaId, String soporteId, String nivelRiesgo, String observacion) {
        Quejas queja = quejasRepository.findById(quejaId)
                .orElseThrow(() -> new RuntimeException("Queja no encontrada"));

        List<String> nivelesValidos = Arrays.asList("BAJO", "MEDIO", "ALTO", "CRITICO");
        if (!nivelesValidos.contains(nivelRiesgo.toUpperCase())) {
            throw new RuntimeException("Nivel de riesgo inválido. Debe ser: BAJO, MEDIO, ALTO o CRITICO");
        }

        String estadoAnterior = obtenerClaveEstado(queja.getEstado_id());

        queja.setNivel_riesgo(nivelRiesgo.toUpperCase());
        queja.setClasificado_por_id(soporteId);
        queja.setFecha_clasificacion(Instant.now());

        Optional<Estados_queja> estadoClasificada = estadosRepository.listar().stream()
                .filter(e -> "CLASIFICADA".equalsIgnoreCase(e.getClave()))
                .findFirst();

        if (estadoClasificada.isPresent()) {
            queja.setEstado_id(estadoClasificada.get().getId());
        }

        Quejas updated = quejasRepository.save(queja);

        historialEventoService.registrar(
                quejaId,
                soporteId,
                "clasificada",
                estadoAnterior,
                "CLASIFICADA",
                "Nivel de riesgo: " + nivelRiesgo + (observacion != null ? " - " + observacion : "")
        );

        return toDTO(updated, soporteId);
    }

    public QuejasDTO cambiarEstadoQueja(String quejaId, String usuarioId, String nuevoEstadoClave, String observacion) {
        System.out.println("🔹 INICIO cambiarEstadoQueja()");
        System.out.println("  - quejaId: " + quejaId);
        System.out.println("  - usuarioId: " + usuarioId);
        System.out.println("  - nuevoEstadoClave: " + nuevoEstadoClave);
        System.out.println("  - observacion: " + observacion);

        try {
            // 1. Buscar queja
            System.out.println("🔹 Buscando queja...");
            Quejas queja = quejasRepository.findById(quejaId)
                    .orElseThrow(() -> new RuntimeException("Queja no encontrada"));
            System.out.println("✅ Queja encontrada: " + queja.getTitulo());

            // 2. Obtener estado anterior
            System.out.println("🔹 Obteniendo estado anterior...");
            String estadoAnterior = obtenerClaveEstado(queja.getEstado_id());
            System.out.println("✅ Estado anterior: " + estadoAnterior);

            // 3. Buscar nuevo estado
            System.out.println("🔹 Buscando nuevo estado...");
            List<Estados_queja> todosEstados = estadosRepository.listar();
            System.out.println("  - Total estados disponibles: " + todosEstados.size());

            Optional<Estados_queja> nuevoEstadoOpt = todosEstados.stream()
                    .filter(e -> {
                        System.out.println("    Comparando: " + e.getClave() + " == " + nuevoEstadoClave);
                        return nuevoEstadoClave.equalsIgnoreCase(e.getClave());
                    })
                    .findFirst();

            if (!nuevoEstadoOpt.isPresent()) {
                System.out.println("❌ Estado no encontrado: " + nuevoEstadoClave);
                throw new RuntimeException("Estado no válido: " + nuevoEstadoClave);
            }

            Estados_queja nuevoEstado = nuevoEstadoOpt.get();
            System.out.println("✅ Nuevo estado encontrado: " + nuevoEstado.getNombre());

            // 4. Actualizar estado
            System.out.println("🔹 Actualizando estado de la queja...");
            queja.setEstado_id(nuevoEstado.getId());

            if ("APROBADA".equalsIgnoreCase(nuevoEstadoClave)) {
                queja.setFecha_aprobacion(Instant.now());
                System.out.println("  - Fecha de aprobación establecida");
            }

            // 5. Guardar queja
            System.out.println("🔹 Guardando queja...");
            Quejas updated = quejasRepository.save(queja);
            System.out.println("✅ Queja guardada");

            // 6. Registrar en historial
            System.out.println("🔹 Registrando en historial...");
            System.out.println("  - quejaId: " + quejaId);
            System.out.println("  - usuarioId: " + usuarioId);
            System.out.println("  - tipoEvento: estado_cambiado");
            System.out.println("  - estadoAnterior: " + estadoAnterior);
            System.out.println("  - estadoNuevo: " + nuevoEstadoClave);

            historialEventoService.registrar(
                    quejaId,
                    usuarioId,
                    "estado_cambiado",
                    estadoAnterior,
                    nuevoEstadoClave,
                    observacion != null ? observacion : "Estado cambiado a " + nuevoEstadoClave
            );
            System.out.println("✅ Historial registrado");

            // 7. Convertir a DTO
            System.out.println("🔹 Convirtiendo a DTO...");
            QuejasDTO dto = toDTO(updated, usuarioId);
            System.out.println("✅ DTO creado");

            return dto;

        } catch (Exception e) {
            System.out.println("❌ ERROR en cambiarEstadoQueja:");
            System.out.println("  - Mensaje: " + e.getMessage());
            System.out.println("  - Tipo: " + e.getClass().getName());
            e.printStackTrace();
            throw new RuntimeException("Error al cambiar estado: " + e.getMessage(), e);
        }
    }

    public List<QuejasDTO> findQuejasAprobadas(String currentUserId) {
        return quejasRepository.findQuejasAprobadas()
                .stream()
                .map(q -> toDTO(q, currentUserId))
                .collect(Collectors.toList());
    }

    public List<QuejasDTO> findQuejasParaRevisar(String currentUserId) {
        return quejasRepository.findQuejasParaRevisar()
                .stream()
                .map(q -> toDTO(q, currentUserId))
                .collect(Collectors.toList());
    }

    public boolean delete(String id) {
        if (!quejasRepository.existsById(id)) {
            return false;
        }
        quejasRepository.deleteById(id);
        return true;
    }

    private String obtenerClaveEstado(String estadoId) {
        if (estadoId == null) {
            return null;
        }

        return estadosRepository.buscarPorId(estadoId)
                .map(Estados_queja::getClave)
                .orElse(null);
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

        dto.setNivel_riesgo(queja.getNivel_riesgo());
        dto.setFecha_clasificacion(queja.getFecha_clasificacion());
        dto.setClasificado_por_id(queja.getClasificado_por_id());
        dto.setFecha_aprobacion(queja.getFecha_aprobacion());

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

        List<ComentariosDTO> comentarios = comentariosRepository.findByQuejaIdActivos(queja.getId())
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
            Optional<com.comunired.tipos_reaccion.domain.entity.Tipos_reaccion> tipoOpt = tiposReaccionRepository.buscarPorId(reaccion.getTipo_reaccion_id());

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

    public QuejasPageDTO findAllPaged(String currentUserId, int page, int size) {
        List<Quejas> todas = quejasRepository.findAll();

        //  Solo mostrar reportes en VOTACION en el feed público
        todas = todas.stream()
                .filter(q -> {
                    String clave = obtenerClaveEstado(q.getEstado_id());
                    return "VOTACION".equalsIgnoreCase(clave);
                })
                .collect(Collectors.toList());

        // Ordenar por fecha_creacion descendente
        todas.sort((a, b) -> {
            if (a.getFecha_creacion() == null) {
                return 1;
            }
            if (b.getFecha_creacion() == null) {
                return -1;
            }
            return b.getFecha_creacion().compareTo(a.getFecha_creacion());
        });

        int total = todas.size();
        int start = page * size;
        int end = Math.min(start + size, total);

        List<QuejasDTO> content = (start >= total)
                ? new ArrayList<>()
                : todas.subList(start, end)
                        .stream()
                        .map(q -> toDTO(q, currentUserId))
                        .collect(Collectors.toList());

        QuejasPageDTO pageDTO = new QuejasPageDTO();
        pageDTO.setContent(content);
        pageDTO.setTotalElements(total);
        pageDTO.setTotalPages((int) Math.ceil((double) total / size));
        pageDTO.setNumber(page);
        pageDTO.setSize(size);
        pageDTO.setLast(end >= total);
        return pageDTO;
    }

    private boolean hasUserVoted(String quejaId, String currentUserId) {
        if (currentUserId == null) {
            return false;
        }

        Optional<String> acceptIdOpt = tiposReaccionRepository.buscarPorKey("accept")
                .map(t -> t.getId());
        Optional<String> rejectIdOpt = tiposReaccionRepository.buscarPorKey("reject")
                .map(t -> t.getId());

        if (acceptIdOpt.isPresent()) {
            Optional<Reacciones> acceptVote = reaccionesRepository
                    .findByQuejaIdAndUsuarioIdAndTipoReaccionId(quejaId, currentUserId, acceptIdOpt.get());
            if (acceptVote.isPresent()) {
                return true;
            }
        }

        if (rejectIdOpt.isPresent()) {
            Optional<Reacciones> rejectVote = reaccionesRepository
                    .findByQuejaIdAndUsuarioIdAndTipoReaccionId(quejaId, currentUserId, rejectIdOpt.get());
            if (rejectVote.isPresent()) {
                return true;
            }
        }

        return false;
    }
}
