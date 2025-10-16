package com.rafaelcaxixi.spring_junit.controllers;

import com.rafaelcaxixi.spring_junit.dtos.AtualizarSenhaRequestDTO;
import com.rafaelcaxixi.spring_junit.dtos.LoginRequestDTO;
import com.rafaelcaxixi.spring_junit.dtos.LoginResponseDTO;
import com.rafaelcaxixi.spring_junit.services.AuthService;
import com.rafaelcaxixi.spring_junit.services.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;

    @PostMapping
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(authService.login(loginRequestDTO));
    }

    @PutMapping("/atualizar-senha/{id}")
    public ResponseEntity<Void> atualizarSenha(@RequestBody @Valid AtualizarSenhaRequestDTO atualizarSenhaRequestDTO, @PathVariable Long id, Authentication authentication) {
        authService.atualizarSenha(atualizarSenhaRequestDTO, id, authentication);
        return ResponseEntity.noContent().build();
    }

}
