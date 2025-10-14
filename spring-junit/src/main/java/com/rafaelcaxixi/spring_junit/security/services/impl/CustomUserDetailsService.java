package com.rafaelcaxixi.spring_junit.security.services.impl;

import com.rafaelcaxixi.spring_junit.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository; // Injeta o seu JPA Repository

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
                return usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + login));
    }
}