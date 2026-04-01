package com.ieshermanosmachado.netbook.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ieshermanosmachado.netbook.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    // Método clave para el Login y registro: buscar por email.
    Optional<Usuario> findByEmail(String email);
    
    boolean existsByEmail(String email);
}
