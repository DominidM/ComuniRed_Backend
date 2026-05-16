package com.comunired.quejas.application.port.out;

import com.comunired.quejas.domain.entity.Queja;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

/**
 * Puertos de salida del dominio quejas. El dominio define qué necesita — la
 * infraestructura lo implementa.
 */
public final class QuejaOutPorts {

    private QuejaOutPorts() {
    }

    // -------------------------------------------------------------------------
    // Persistencia
    // -------------------------------------------------------------------------
    public interface QuejaRepositoryPort {

        Queja guardar(Queja queja);

        Optional<Queja> buscarPorId(String id);

        List<Queja> buscarTodas();

        List<Queja> buscarPorUsuarioId(String usuarioId);

        List<Queja> buscarAprobadas();

        List<Queja> buscarParaRevisar();

        boolean existePorId(String id);

        Page<Queja> buscarTodasPaginado(org.springframework.data.domain.Pageable pageable);

        void eliminar(String id);

        // NUEVO
        org.springframework.data.domain.Page<Queja> buscarPorEstadosClavePaginado(
                List<String> estadoIds, int page, int size);
    }    // -------------------------------------------------------------------------
    // Cloudinary — subida de imagen
    // -------------------------------------------------------------------------

    public interface QuejaImagenPort {

        String subirImagen(org.springframework.web.multipart.MultipartFile imagen);

        void eliminarImagen(String imagenUrl);
    }

    // -------------------------------------------------------------------------
    // Consultas a otros dominios (solo lo que quejas necesita, nada más)
    // -------------------------------------------------------------------------
    public interface EstadoQuejaPort {

        Optional<EstadoInfo> buscarPorClave(String clave);

        Optional<EstadoInfo> buscarPorId(String id);

        java.util.Map<String, EstadoInfo> buscarPorIds(java.util.List<String> ids);

        record EstadoInfo(String id, String clave, String nombre) {

        }
    }

    public interface UsuarioQuejaPort {

        Optional<UsuarioInfo> buscarPorId(String usuarioId);

        java.util.Map<String, UsuarioInfo> buscarPorIds(java.util.List<String> ids);

        record UsuarioInfo(String id, String nombre, String apellido, String fotoPerfil) {

        }
    }

    public interface CategoriaQuejaPort {

        Optional<CategoriaInfo> buscarPorId(String categoriaId);

        java.util.Map<String, CategoriaInfo> buscarPorIds(java.util.List<String> ids);

        record CategoriaInfo(String id, String nombre, String descripcion) {

        }
    }

    public interface VotoQuejaPort {

        long contarVotosSi(String quejaId);

        long contarVotosNo(String quejaId);

        boolean yaVoto(String quejaId, String usuarioId);

        Optional<String> obtenerVotoUsuario(String quejaId, String usuarioId);

        record VotoCounts(long si, long no) {}

        java.util.Map<String, VotoCounts> contarVotosPorQuejaIds(java.util.List<String> quejaIds);

        java.util.Map<String, String> obtenerVotosUsuarioPorQuejaIds(java.util.List<String> quejaIds, String usuarioId);
    }

    public interface ReaccionQuejaPort {

        java.util.Map<String, Long> contarReacciones(String quejaId);

        Optional<String> obtenerReaccionUsuario(String quejaId, String usuarioId);

        java.util.Map<String, java.util.Map<String, Long>> contarReaccionesPorQuejaIds(java.util.List<String> quejaIds);

        java.util.Map<String, String> obtenerReaccionesUsuarioPorQuejaIds(java.util.List<String> quejaIds, String usuarioId);
    }

    public interface ComentarioQuejaPort {

        List<ComentarioInfo> buscarPorQueja(String quejaId);

        java.util.Map<String, List<ComentarioInfo>> buscarPorQuejaIds(java.util.List<String> quejaIds);

        record ComentarioInfo(
                String id,
                String texto,
                String usuarioId,
                String usuarioNombre,
                String usuarioApellido,
                String usuarioFoto,
                java.time.Instant fechaCreacion
                ) {

        }
    }

    public interface EvidenciaQuejaPort {

        List<EvidenciaInfo> buscarPorQueja(String quejaId);

        record EvidenciaInfo(String id, String url, String tipo) {

        }
    }
}
