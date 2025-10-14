package com.rafaelcaxixi.spring_junit.controllers;

import com.rafaelcaxixi.spring_junit.dtos.UsuarioRequestDto;
import com.rafaelcaxixi.spring_junit.dtos.UsuarioResponseDto;
import com.rafaelcaxixi.spring_junit.exceptions.ResourceNotFoundException;
import com.rafaelcaxixi.spring_junit.security.CustomAccessDeniedHandler;
import com.rafaelcaxixi.spring_junit.security.CustomAuthenticationEntryPoint;
import com.rafaelcaxixi.spring_junit.security.SecurityConfig;
import com.rafaelcaxixi.spring_junit.security.SecurityFilter;
import com.rafaelcaxixi.spring_junit.security.services.TokenService;
import com.rafaelcaxixi.spring_junit.services.UsuarioService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @MockBean
    private SecurityFilter securityFilter;

    @MockBean
    private UsuarioService usuarioService;

    private UsuarioRequestDto usuarioRequestDto;

    @BeforeEach
    void setUp() {
        usuarioRequestDto = new UsuarioRequestDto("Rafael Caxixi", "rafael@gmail.com", 20,"123456");
    }


    @Test
    void criarUsuarioComSucesso() throws Exception {
        //ARRANGE
        UsuarioResponseDto usuarioResponseDto = new UsuarioResponseDto(1L, "Rafael Caxixi", "rafael@gmail.com", 34);
        when(usuarioService.cadastrarUsuario(any(UsuarioRequestDto.class))).thenReturn(usuarioResponseDto);


        String jsonRequest = """
        {
            "login": "Rafael Caxixi",
            "email": "rafael@gmail.com",
            "idade": 34,
            "senha": "123456"
        }
        """;

        //ACT
        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("rafael@gmail.com"))
                .andExpect(jsonPath("$.login").value("Rafael Caxixi"));

//
//        //ASSERT
//        assertNotNull(usuarioResponseDto);
//        assertEquals(1L, usuarioResponseDto.id());
//        assertEquals("rafael@gmail.com", usuarioResponseDto.email());
    }

    @Test
    void criarUsuarioComJsonVazio() throws Exception {
        //ARRANGE
        String json = """
                        {}
                """;

        //ACT + ASSERT
        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void buscarUsuarioPorIdComSucesso() throws Exception {
        //ARRANGE
        UsuarioResponseDto usuarioResponseDto = new UsuarioResponseDto(1L, "Rafael Caxixi", "rafael@gmail.com", 34);
        when(usuarioService.buscarUsuarioPorId(1L)).thenReturn(usuarioResponseDto);

        //ACT
        mockMvc.perform(get("/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        //ASSERT
        assertNotNull(usuarioResponseDto);
        assertEquals(1L, usuarioResponseDto.id());
        assertEquals("rafael@gmail.com", usuarioResponseDto.email());
    }

    @Test
    void buscarUsuarioPorIdComUsuarioInexistente() throws Exception {
        //ARRANGE
        UsuarioResponseDto usuarioResponseDto = new UsuarioResponseDto(1L, "Rafael Caxixi", "rafael@gmail.com", 34);
        when(usuarioService.buscarUsuarioPorId(1L)).thenThrow(new ResourceNotFoundException("Usuário não encontrado"));

        //ACT
        mockMvc.perform(get("/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void atualizarUsuarioComSucesso() throws Exception {
        //ARRANGE
        UsuarioResponseDto usuarioResponseDto = new UsuarioResponseDto(1L, "Rafael Caxixi", "rafael@gmail.com", 34);
        when(usuarioService.atualizarUsuario(1L,usuarioRequestDto)).thenReturn(usuarioResponseDto);

        String jsonRequest = """
        {
            "login": "Rafael Caxixi",
            "email": "rafael@gmail.com",
            "idade": 34,
            "senha": "123456"
        }
        """;

        //ACT + ASSERT
        mockMvc.perform(put("/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk());

    }

    @Test
    void deletarUsuarioComSucesso() throws Exception {
        //ARRANGE
        doNothing().when(usuarioService).deletarUsuario(1L);

        //ACT + ASSERT
        mockMvc.perform(delete("/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}