package com.rafaelcaxixi.spring_junit.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioRequestDto(
        @NotBlank
        String nome,
        @NotBlank
        String email
) {
}
