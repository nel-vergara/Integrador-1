package com.medina.fragrances.fragrance_app.repository;

import com.medina.fragrances.fragrance_app.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Buscar usuario por email (para el login)
    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findFirstByRol(String rol);

    Optional<Usuario> findByDni(String dni);

    // Verificar si ya existe un email registrado
    boolean existsByEmail(String email);

    // Verificar si ya existe un DNI registrado
    boolean existsByDni(String dni);
}
