package com.rafaelcaxixi.spring_junit.controllers;

import com.rafaelcaxixi.spring_junit.dtos.UsuarioRequestDto;
import com.rafaelcaxixi.spring_junit.dtos.UsuarioResponseDto;
import com.rafaelcaxixi.spring_junit.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Criar usuário", description = "Cria um usuário e retorna o dto de resposta")
    @PostMapping
    public ResponseEntity<UsuarioResponseDto> criarUsuario(@Valid @RequestBody UsuarioRequestDto dto) {
        UsuarioResponseDto usuarioResponseDto = usuarioService.cadastrarUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioResponseDto);
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Buscar usuários", description = "Retorna todos os usuários")
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> buscarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Buscar usuário por id", description = "Retorna o usuário pelo id")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> buscarUsuarioPorId(@PathVariable Long id) {
        UsuarioResponseDto usuarioPorId = usuarioService.buscarUsuarioPorId(id);
        return ResponseEntity.ok(usuarioPorId);
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Atualizar usuário", description = "Atualiza o usuário")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> atualizarUsuario(@Valid @PathVariable Long id, @RequestBody UsuarioRequestDto dto) {
        UsuarioResponseDto usuarioResponseDto = usuarioService.atualizarUsuario(id, dto);
        return ResponseEntity.ok(usuarioResponseDto);
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Deletar usuário", description = "Deleta o usuário pelo id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
