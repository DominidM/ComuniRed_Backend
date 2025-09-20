package com.comunired.usuarios.infrastructure.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.comunired.usuarios.domain.entity.Usuario;
import com.comunired.usuarios.domain.repository.UsuariosRepository;

@Repository
public class UsuariosRepositoryImpl implements UsuariosRepository {
    private final UsuarioMongoRepository usuarioMongoRepository;

    public UsuariosRepositoryImpl(UsuarioMongoRepository usuarioMongoRepository) {
        this.usuarioMongoRepository = usuarioMongoRepository;
    }

    @Override
    public List<Usuario> findAll() {
        return usuarioMongoRepository.findAll();
    }

    @Override
    public Page<Usuario> findAll(Pageable pageable) {
        return usuarioMongoRepository.findAll(pageable);
    }

    @Override
    public Usuario save(Usuario usuario) {
        return usuarioMongoRepository.save(usuario);
    }

    @Override
    public Usuario findByEmail(String email) {
        return usuarioMongoRepository.findByEmail(email);
    }

    @Override
    public Usuario findById(String id) {
        return usuarioMongoRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(String id) {
        usuarioMongoRepository.deleteById(id);
    }

    @Override
    public long countByRolId(String rol_id) {
        return usuarioMongoRepository.countByRolId(rol_id); 
    }
}