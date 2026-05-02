package com.comunired.historias.application.port.in;

import com.comunired.historias.application.dto.out.HistoriaResponse;
import java.util.List;

public interface ObtenerHistoriasUseCase {
    List<HistoriaResponse> obtenerActivas(String distrito);
    HistoriaResponse marcarVista(String historiaId, String usuarioId);
}