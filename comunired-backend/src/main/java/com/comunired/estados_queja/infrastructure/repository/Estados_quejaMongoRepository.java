package com.comunired.estados_queja.infrastructure.repository;

import com.comunired.estados_queja.domain.entity.Estados_queja;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface Estados_quejaMongoRepository extends MongoRepository<Estados_queja, String> {
    Optional<Estados_queja> findByNombre(String nombre);
    Page<Estados_queja> findAll(Pageable pageable);

}
