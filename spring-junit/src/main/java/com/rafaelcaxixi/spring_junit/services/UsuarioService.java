package com.rafaelcaxixi.spring_junit.services;

import com.rafaelcaxixi.spring_junit.domains.Usuario;
import com.rafaelcaxixi.spring_junit.dtos.UsuarioRequestDto;
import com.rafaelcaxixi.spring_junit.dtos.UsuarioResponseDto;
import com.rafaelcaxixi.spring_junit.exceptions.ResourceNotFoundException;
import com.rafaelcaxixi.spring_junit.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioResponseDto cadastrarUsuario(UsuarioRequestDto dto) {
        try {
            if (existsByEmail(dto.email())) {
                throw new DataIntegrityViolationException("Email já cadastrado");
            }
            Usuario usuario = usuarioRepository.save(new Usuario(dto.nome(), dto.email(), dto.idade()));
            return new UsuarioResponseDto(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getIdade());
        } catch (DataIntegrityViolationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }

    public boolean existsByEmail(String email) {
        try {
            return usuarioRepository.existsByEmail(email);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao verificar existência de email: " + e.getMessage());
        }
    }

    public List<UsuarioResponseDto> listarUsuarios() {
        try {
            List<Usuario> usuarios = usuarioRepository.findAll();
            return usuarios.stream()
                    .map(usuario -> new UsuarioResponseDto(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getIdade()))
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar usuários: " + e.getMessage());
        }
    }

    public UsuarioResponseDto buscarUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        return new UsuarioResponseDto(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getIdade());
    }


    public UsuarioResponseDto atualizarUsuario(Long id, UsuarioRequestDto dto) {
        try {
            Usuario usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            usuario.setNome(dto.nome());
            usuario.setEmail(dto.email());
            Usuario usuarioAtualizado = usuarioRepository.save(usuario);
            return new UsuarioResponseDto(usuarioAtualizado.getId(), usuarioAtualizado.getNome(), usuarioAtualizado.getEmail(), usuario.getIdade());
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
