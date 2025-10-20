package com.comunired.estados_queja.infrastructure.repository;

import com.comunired.estados_queja.domain.entity.Estados_queja;
import com.comunired.estados_queja.domain.repository.Estados_quejaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class Estados_quejaRepositoryImpl implements Estados_quejaRepository {

    private final Estados_quejaMongoRepository mongoRepository;

    public Estados_quejaRepositoryImpl(Estados_quejaMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Estados_queja guardar(Estados_queja estado) {
        return mongoRepository.save(estado);
    }

    @Override
    public Estados_queja modificar(Estados_queja estado) {
        return mongoRepository.save(estado);
    }

    @Override
    public Optional<Estados_queja> buscarPorId(String id) {
        return mongoRepository.findById(id);
    }

    @Override
    public Optional<Estados_queja> buscarPorNombre(String nombre) {
        return mongoRepository.findByNombre(nombre);
    }

    @Override
    public List<Estados_queja> listar() {
        return mongoRepository.findAll();
    }

    @Override
    public void eliminar(String id) {
        mongoRepository.deleteById(id);
    }
}
