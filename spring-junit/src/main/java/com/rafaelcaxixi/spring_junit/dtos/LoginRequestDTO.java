package com.rafaelcaxixi.spring_junit.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank
        String login,
        @NotBlank
        String senha
) {
}
