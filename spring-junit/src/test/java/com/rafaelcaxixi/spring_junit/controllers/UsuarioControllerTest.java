package com.rafaelcaxixi.spring_junit.controllers;

import com.rafaelcaxixi.spring_junit.dtos.UsuarioRequestDto;
import com.rafaelcaxixi.spring_junit.dtos.UsuarioResponseDto;
import com.rafaelcaxixi.spring_junit.exceptions.ResourceNotFoundException;
import com.rafaelcaxixi.spring_junit.services.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
        usuarioRequestDto = new UsuarioRequestDto("Rafael Caxixi", "usuarioteste@gmail.com", 20);
    }

    @Test
    void criarUsuarioComSucesso() throws Exception {
        //ARRANGE
        UsuarioResponseDto usuarioResponseDto = new UsuarioResponseDto(1L, "Rafael Caxixi", "rafael@gmail.com", 34);
        when(usuarioService.cadastrarUsuario(usuarioRequestDto)).thenReturn(usuarioResponseDto);

        //ACT
        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Rafael Caxixi\",\"email\":\"usuarioteste@gmail.com\",\"idade\":20}"))
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
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Rafael Caxixi\",\"email\":\"usuarioteste@gmail.com\",\"idade\":20}"))
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
        UsuarioResponseDto responseDTO = mock(UsuarioResponseDto.class);

        when(usuarioService.atualizarUsuario(1L,usuarioRequestDto)).thenReturn(responseDTO);

        ResponseEntity<UsuarioResponseDto> response = usuarioController.atualizarUsuario(1L, usuarioRequestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
//
//        //ARRANGE
//        UsuarioResponseDto usuarioResponseDto = new UsuarioResponseDto(1L, "Rafael Caxixi", "rafael@gmail.com", 34);
//        when(usuarioService.atualizarUsuario(1L,usuarioRequestDto)).thenReturn(usuarioResponseDto);
//
//        //ACT
//        mockMvc.perform(put("/usuarios/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(usuarioRequestDto.toString()))
//                .andExpect(status().isOk());
//
//        assertNotNull(usuarioResponseDto);
//        assertEquals(1L, usuarioResponseDto.id());
//        assertEquals("usuarioteste@gmail.com", usuarioResponseDto.email());
    }

    @Test
    void deletarUsuario() {
    }
}