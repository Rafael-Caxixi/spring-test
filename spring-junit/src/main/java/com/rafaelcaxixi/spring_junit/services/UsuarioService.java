package com.rafaelcaxixi.spring_junit.services;

import com.rafaelcaxixi.spring_junit.domains.Usuario;
import com.rafaelcaxixi.spring_junit.dtos.LoginRequestDTO;
import com.rafaelcaxixi.spring_junit.dtos.LoginResponseDTO;
import com.rafaelcaxixi.spring_junit.dtos.UsuarioRequestDto;
import com.rafaelcaxixi.spring_junit.dtos.UsuarioResponseDto;
import com.rafaelcaxixi.spring_junit.exceptions.ResourceNotFoundException;
import com.rafaelcaxixi.spring_junit.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public UsuarioResponseDto cadastrarUsuario(UsuarioRequestDto dto) {
        try {
            if (existsByLogin(dto.login())) {
                throw new DataIntegrityViolationException("Login já cadastrado");
            }
            String senhaCriptografada = passwordEncoder.encode(dto.senha());

            Usuario usuario = usuarioRepository.save(new Usuario(dto.login(), dto.email(), dto.idade(), senhaCriptografada));
            return new UsuarioResponseDto(usuario.getId(), usuario.getLogin(), usuario.getEmail(), usuario.getIdade());
        } catch (DataIntegrityViolationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }

    public boolean existsByLogin(String login) {
        try {
            return usuarioRepository.existsByLogin(login);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao verificar existência de login: " + e.getMessage());
        }
    }

    public List<UsuarioResponseDto> listarUsuarios() {
        try {
            List<Usuario> usuarios = usuarioRepository.findAll();
            return usuarios.stream()
                    .map(usuario -> new UsuarioResponseDto(usuario.getId(), usuario.getLogin(), usuario.getEmail(), usuario.getIdade()))
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar usuários: " + e.getMessage());
        }
    }

    public UsuarioResponseDto buscarUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        return new UsuarioResponseDto(usuario.getId(), usuario.getLogin(), usuario.getEmail(), usuario.getIdade());
    }


    public UsuarioResponseDto atualizarUsuario(Long id, UsuarioRequestDto dto) {
        try {
            Usuario usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            usuario.setLogin(dto.login());
            usuario.setEmail(dto.email());
            usuario.setIdade(dto.idade());
            Usuario usuarioAtualizado = usuarioRepository.save(usuario);
            return new UsuarioResponseDto(usuarioAtualizado.getId(), usuarioAtualizado.getLogin(), usuarioAtualizado.getEmail(), usuario.getIdade());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    public void deletarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        usuarioRepository.delete(usuario);
    }



}
