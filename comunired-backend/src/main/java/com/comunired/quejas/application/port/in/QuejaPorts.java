package com.comunired.quejas.application.port.in;

import com.comunired.quejas.application.dto.in.QuejaCommands.*;
import com.comunired.quejas.application.dto.out.QuejaResponses.*;

// -----------------------------------------------------------------------------
// COMMANDS — escritura
// -----------------------------------------------------------------------------

public final class QuejaPorts {

    private QuejaPorts() {}

    public interface CrearQuejaUseCase {
        QuejaResponse ejecutar(CrearQuejaCommand command);
    }

    public interface ActualizarQuejaUseCase {
        QuejaResponse ejecutar(ActualizarQuejaCommand command);
    }

    public interface ClasificarRiesgoUseCase {
        QuejaResponse ejecutar(ClasificarRiesgoCommand command);
    }

    public interface CambiarEstadoQuejaUseCase {
        QuejaResponse ejecutar(CambiarEstadoCommand command);
    }

    public interface EliminarQuejaUseCase {
        boolean ejecutar(EliminarQuejaCommand command);
    }

    // -------------------------------------------------------------------------
    // QUERIES — lectura
    // -------------------------------------------------------------------------

    public interface ObtenerQuejaUseCase {
        QuejaResponse ejecutar(String quejaId, String usuarioActualId);
    }

    public interface ListarQuejasUseCase {
        java.util.List<QuejaResponse> ejecutar(String usuarioActualId);
    }

    public interface ListarQuejasPaginadasUseCase {
        QuejaPageResponse ejecutar(String usuarioActualId, int page, int size);
    }

    public interface ListarQuejasPorUsuarioUseCase {
        java.util.List<QuejaResponse> ejecutar(String usuarioId, String usuarioActualId);
    }

    public interface ListarQuejasAprobadasUseCase {
        java.util.List<QuejaResponse> ejecutar(String usuarioActualId);
    }

    public interface ListarQuejasParaRevisarUseCase {
        java.util.List<QuejaResponse> ejecutar(String usuarioActualId);
    }
}
