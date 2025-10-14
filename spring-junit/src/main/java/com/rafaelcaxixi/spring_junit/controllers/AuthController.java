package com.rafaelcaxixi.spring_junit.controllers;

import com.rafaelcaxixi.spring_junit.dtos.LoginRequestDTO;
import com.rafaelcaxixi.spring_junit.services.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return ResponseEntity.ok(usuarioService.login(loginRequestDTO));
    }

}
