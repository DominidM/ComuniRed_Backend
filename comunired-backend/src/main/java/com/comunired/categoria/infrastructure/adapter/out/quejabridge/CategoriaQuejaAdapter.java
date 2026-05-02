package com.comunired.categoria.infrastructure.adapter.out.quejabridge;

import com.comunired.categoria.infrastructure.repository.CategoriaMongoRepository;
import com.comunired.quejas.application.port.out.QuejaOutPorts.CategoriaQuejaPort;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class CategoriaQuejaAdapter implements CategoriaQuejaPort {

    private final CategoriaMongoRepository repo;

    public CategoriaQuejaAdapter(CategoriaMongoRepository repo) {
        this.repo = repo;
    }

    @Override
    public Optional<CategoriaInfo> buscarPorId(String categoriaId) {
        return repo.findById(categoriaId)
                .map(c -> new CategoriaInfo(c.getId(), c.getNombre(), c.getDescripcion()));
    }
}