package com.comunired.historial_evento.graphql.resolver;

import com.comunired.historial_evento.application.service.HistorialEventoService;
import com.comunired.historial_evento.application.dto.HistorialEventoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;

@Controller
public class HistorialEventoResolver {

    @Autowired
    private HistorialEventoService historialEventoService;

    @QueryMapping
    public List<HistorialEventoDTO> historialPorQueja(@Argument String quejaId) {
        return historialEventoService.findByQuejaId(quejaId);
    }

    @QueryMapping
    public List<HistorialEventoDTO> historialPorTipo(@Argument String quejaId, @Argument String tipoEvento) {
        return historialEventoService.findByQuejaIdAndTipo(quejaId, tipoEvento);
    }

    @QueryMapping
    public List<HistorialEventoDTO> historialReciente(@Argument String quejaId, @Argument Integer limite) {
        if (limite == null) {
            limite = 10;
        }
        return historialEventoService.findRecentByQuejaId(quejaId, limite);
    }
}
