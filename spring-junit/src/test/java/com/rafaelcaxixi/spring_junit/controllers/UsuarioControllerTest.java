package com.rafaelcaxixi.spring_junit.controllers;

import com.rafaelcaxixi.spring_junit.dtos.UsuarioRequestDto;
import com.rafaelcaxixi.spring_junit.dtos.UsuarioResponseDto;
import com.rafaelcaxixi.spring_junit.exceptions.ResourceNotFoundException;
import com.rafaelcaxixi.spring_junit.services.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @InjectMocks
    private UsuarioController usuarioController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    private UsuarioRequestDto usuarioRequestDto;

    @BeforeEach
    void setUp() {
        usuarioRequestDto = new UsuarioRequestDto("Rafael Caxixi", "rafael@gmail.com", 20);
    }

    @Test
    void criarUsuarioComSucesso() throws Exception {
        //ARRANGE
        UsuarioResponseDto usuarioResponseDto = new UsuarioResponseDto(1L, "Rafael Caxixi", "rafael@gmail.com", 34);
        when(usuarioService.cadastrarUsuario(usuarioRequestDto)).thenReturn(usuarioResponseDto);

        String jsonRequest = """
        {
            "nome": "Rafael Caxixi",
            "email": "rafael@gmail.com",
            "idade": 34
        }
        """;

        //ACT
        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated());

        //ASSERT
        assertNotNull(usuarioResponseDto);
        assertEquals(1L, usuarioResponseDto.id());
        assertEquals("rafael@gmail.com", usuarioResponseDto.email());
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
            "nome": "Rafael Caxixi",
            "email": "rafael@gmail.com",
            "idade": 34
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