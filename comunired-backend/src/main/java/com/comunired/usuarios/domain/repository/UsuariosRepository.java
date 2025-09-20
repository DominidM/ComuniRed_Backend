package com.comunired.usuarios.domain.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.comunired.usuarios.domain.entity.Usuario;

public interface UsuariosRepository {
    List<Usuario> findAll();
    Page<Usuario> findAll(Pageable pageable);
    Usuario save(Usuario usuario);
    Usuario findByEmail(String email);
    Usuario findById(String id);
    void deleteById(String id);
    long countByRolId(String rol_id);
}