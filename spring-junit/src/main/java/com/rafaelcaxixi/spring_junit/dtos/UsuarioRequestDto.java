package com.rafaelcaxixi.spring_junit.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioRequestDto(
        @NotBlank(message = "O login não pode ser vazio")
        String login,
        @NotBlank(message = "O email não pode ser vazio")
        String email,
        @NotNull(message = "A idade precisa ser informada corretamente")
        Integer idade,
        @NotBlank(message = "A senha não pode ser vazio")
        String senha
) {
}
