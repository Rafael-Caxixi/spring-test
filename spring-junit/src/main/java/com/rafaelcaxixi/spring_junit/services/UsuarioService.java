package com.rafaelcaxixi.spring_junit.services;

import com.rafaelcaxixi.spring_junit.domains.Usuario;
import com.rafaelcaxixi.spring_junit.dtos.UsuarioRequestDto;
import com.rafaelcaxixi.spring_junit.dtos.UsuarioResponseDto;
import com.rafaelcaxixi.spring_junit.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioResponseDto cadastrarUsuario(UsuarioRequestDto dto) {
        try {
            if (usuarioRepository.existsByEmail(dto.email())) {
                throw new RuntimeException("Email já cadastrado");
            }
            Usuario usuario = usuarioRepository.save(new Usuario(dto.nome(), dto.email()));
            return new UsuarioResponseDto(usuario.getId(), usuario.getNome(), usuario.getEmail());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }

    public List<UsuarioResponseDto> listarUsuarios(){
        try{
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream()
                .map(usuario -> new UsuarioResponseDto(usuario.getId(), usuario.getNome(), usuario.getEmail()))
                .toList();
        }catch (Exception e){
            throw new RuntimeException("Erro ao listar usuários: " + e.getMessage());
        }
    }

    public UsuarioResponseDto buscarUsuarioPorId(Long id){
        try{
            Usuario usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            return new UsuarioResponseDto(usuario.getId(), usuario.getNome(), usuario.getEmail());
        }catch (Exception e){
            throw new RuntimeException("Erro ao buscar usuário por ID: " + e.getMessage());
        }
    }

    public UsuarioResponseDto atualizarUsuario(Long id, UsuarioRequestDto dto){
        try{
            Usuario usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            usuario.setNome(dto.nome());
            usuario.setEmail(dto.email());
            Usuario usuarioAtualizado = usuarioRepository.save(usuario);
            return new UsuarioResponseDto(usuarioAtualizado.getId(), usuarioAtualizado.getNome(), usuarioAtualizado.getEmail());
        }catch (Exception e){
            throw new RuntimeException("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    public void deletarUsuario(Long id){
        try{
            Usuario usuario = usuarioRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
            usuarioRepository.delete(usuario);
        }catch (Exception e){
            throw new RuntimeException("Erro ao deletar usuário: " + e.getMessage());
        }
    }


}
