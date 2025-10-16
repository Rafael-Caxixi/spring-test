package com.rafaelcaxixi.spring_junit.dtos;

import jakarta.validation.constraints.NotBlank;

public record   AtualizarSenhaRequestDTO(
        @NotBlank(message = "Senha atual da pessoa precisa estar preenchido")
        String senhaAtual,
        @NotBlank(message = "Nova senha da pessoa precisa estar preenchido")
        String novaSenha
) {
}
