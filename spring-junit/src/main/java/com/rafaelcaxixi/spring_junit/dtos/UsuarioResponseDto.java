package com.rafaelcaxixi.spring_junit.dtos;

public record UsuarioResponseDto(

        Long id,
        String login,
        String email,
        Integer idade

) {
}
