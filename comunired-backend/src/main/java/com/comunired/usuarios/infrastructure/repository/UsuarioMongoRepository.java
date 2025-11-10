package com.comunired.usuarios.infrastructure.repository;

import com.comunired.usuarios.domain.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioMongoRepository extends MongoRepository<Usuario, String> {
    Usuario findByEmail(String email);
    
    @Query(value = "{'rol_id': ?0}", count = true)
    long countByRolId(String rol_id);

    @Query("{ $or: [ " +
        "{ 'nombre': { $regex: ?0, $options: 'i' } }, " +
        "{ 'apellido': { $regex: ?0, $options: 'i' } }, " +
        "{ 'email': { $regex: ?0, $options: 'i' } } " +
        "] }")
    Page<Usuario> buscarPorTermino(String termino, Pageable pageable);

    @Query("{ $or: [ " +
        "{ 'nombre': { $regex: ?0, $options: 'i' } }, " +
        "{ 'apellido': { $regex: ?0, $options: 'i' } } " +
        "] }")
    Page<Usuario> buscarPorNombre(String nombre, Pageable pageable);

    Page<Usuario> findByIdNotIn(List<String> ids, Pageable pageable);
}
