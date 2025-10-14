package com.rafaelcaxixi.spring_junit.repositories;

import com.rafaelcaxixi.spring_junit.domains.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByEmail(String login);

    Optional<Usuario> findByLogin(String login);

}
