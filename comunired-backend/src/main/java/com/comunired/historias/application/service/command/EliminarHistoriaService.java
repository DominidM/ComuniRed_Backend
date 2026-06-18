package com.comunired.historias.application.service.command;

import com.comunired.historias.application.port.in.EliminarHistoriaUseCase;
import com.comunired.historias.application.port.out.HistoriaRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class EliminarHistoriaService implements EliminarHistoriaUseCase {

    private final HistoriaRepositoryPort repositoryPort;

    public EliminarHistoriaService(HistoriaRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public void ejecutar(String historiaId) {
        repositoryPort.eliminarPorId(historiaId);
    }
}
