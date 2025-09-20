package com.comunired.usuarios.infrastructure.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.comunired.usuarios.domain.entity.Usuario;

@Repository
public interface UsuarioMongoRepository extends MongoRepository<Usuario, String> {
    Usuario findByEmail(String email);
    
    @Query(value = "{'rol_id': ?0}", count = true)
    long countByRolId(String rol_id);
}