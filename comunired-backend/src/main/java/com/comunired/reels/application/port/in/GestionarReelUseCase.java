package com.comunired.reels.application.port.in;

import com.comunired.reels.application.dto.in.ActualizarReelCommand;
import com.comunired.reels.application.dto.in.CrearReelCommand;
import com.comunired.reels.application.dto.out.ReelResponse;

import java.util.List;

public interface GestionarReelUseCase {
    List<ReelResponse> listarTodos(String termino);
    ReelResponse obtenerPorId(String id);
    ReelResponse crear(CrearReelCommand command);
    ReelResponse actualizar(String id, ActualizarReelCommand command);
    void eliminar(String id);
}
