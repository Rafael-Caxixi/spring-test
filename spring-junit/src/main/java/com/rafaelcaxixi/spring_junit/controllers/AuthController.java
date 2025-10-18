package com.rafaelcaxixi.spring_junit.controllers;

import com.rafaelcaxixi.spring_junit.dtos.AtualizarSenhaRequestDTO;
import com.rafaelcaxixi.spring_junit.dtos.LoginRequestDTO;
import com.rafaelcaxixi.spring_junit.dtos.LoginResponseDTO;
import com.rafaelcaxixi.spring_junit.services.AuthService;
import com.rafaelcaxixi.spring_junit.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Fazer login", description = "Faz o login e retorna o token de autenticação")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        LoginResponseDTO token = authService.login(dto);
        return ResponseEntity.ok(token);
    }

    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Atualizar senha", description = "Atualiza a senha do usuário")
    @PutMapping("/atualizar-senha/{id}")
    public ResponseEntity<Void> atualizarSenha(@RequestBody @Valid AtualizarSenhaRequestDTO atualizarSenhaRequestDTO, @PathVariable Long id, Authentication authentication) {
        authService.atualizarSenha(atualizarSenhaRequestDTO, id, authentication);
        return ResponseEntity.noContent().build();
    }

}
