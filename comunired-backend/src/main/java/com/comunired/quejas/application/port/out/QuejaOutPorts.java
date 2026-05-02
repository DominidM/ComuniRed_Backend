package com.comunired.quejas.application.port.out;

import com.comunired.quejas.domain.entity.Queja;

import java.util.List;
import java.util.Optional;

/**
 * Puertos de salida del dominio quejas.
 * El dominio define qué necesita — la infraestructura lo implementa.
 */
public final class QuejaOutPorts {

    private QuejaOutPorts() {}

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
        void eliminar(String id);
    }

    // -------------------------------------------------------------------------
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

        record EstadoInfo(String id, String clave, String nombre) {}
    }

    public interface UsuarioQuejaPort {
        Optional<UsuarioInfo> buscarPorId(String usuarioId);

        record UsuarioInfo(String id, String nombre, String apellido, String fotoPerfil) {}
    }

    public interface CategoriaQuejaPort {
        Optional<CategoriaInfo> buscarPorId(String categoriaId);

        record CategoriaInfo(String id, String nombre, String descripcion) {}
    }

    public interface VotoQuejaPort {
        long contarVotosSi(String quejaId);
        long contarVotosNo(String quejaId);
        boolean yaVoto(String quejaId, String usuarioId);
        Optional<String> obtenerVotoUsuario(String quejaId, String usuarioId);
    }

    public interface ReaccionQuejaPort {
        java.util.Map<String, Long> contarReacciones(String quejaId);
        Optional<String> obtenerReaccionUsuario(String quejaId, String usuarioId);
    }

    public interface ComentarioQuejaPort {
        List<ComentarioInfo> buscarPorQueja(String quejaId);

        record ComentarioInfo(
                String id,
                String texto,
                String usuarioId,
                String usuarioNombre,
                String usuarioApellido,
                String usuarioFoto,
                java.time.Instant fechaCreacion
        ) {}
    }

    public interface EvidenciaQuejaPort {
        List<EvidenciaInfo> buscarPorQueja(String quejaId);

        record EvidenciaInfo(String id, String url, String tipo) {}
    }
}
