package com.comunired.historias.application.port.in;

import com.comunired.historias.application.dto.in.CrearHistoriaCommand;
import com.comunired.historias.application.dto.out.HistoriaResponse;

public interface CrearHistoriaUseCase {
    HistoriaResponse ejecutar(CrearHistoriaCommand command);
}