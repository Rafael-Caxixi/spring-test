package com.rafaelcaxixi.spring_junit.repositories;

import com.rafaelcaxixi.spring_junit.domains.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByEmail(String email);
}
