package com.rafaelcaxixi.spring_junit.services;

import com.rafaelcaxixi.spring_junit.domains.Usuario;
import com.rafaelcaxixi.spring_junit.dtos.UsuarioRequestDto;
import com.rafaelcaxixi.spring_junit.dtos.UsuarioResponseDto;
import com.rafaelcaxixi.spring_junit.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private UsuarioRequestDto usuarioRequestDto;

    @BeforeEach
    void setUp() {
        usuarioRequestDto = new UsuarioRequestDto("Rafael Caxixi", "usuarioteste@gmail.com",20);
    }


    @Test
    void cadastrarUsuarioComSucesso() {
        //ARRANGE
        when(usuarioRepository.existsByEmail(usuarioRequestDto.email())).thenReturn(false);
        Usuario usuario = new Usuario("Rafael Caxixi","usuarioteste@gmail.com",20);
        usuario.setId(1L);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        //ACT
        UsuarioResponseDto usuarioResponseDto = usuarioService.cadastrarUsuario(usuarioRequestDto);

        //ASSERT
        assertNotNull(usuarioResponseDto);
        assertEquals("usuarioteste@gmail.com", usuarioResponseDto.email());
        then(usuarioRepository).should(times(1)).save(any(Usuario.class));
    }

    @Test
    void cadastrarUsuarioComEmailExistente() {
        //ARRANGE + ACT
        when(usuarioRepository.existsByEmail(usuarioRequestDto.email())).thenReturn(true);
        DataIntegrityViolationException exception = assertThrows(
                DataIntegrityViolationException.class,
                () -> usuarioService.cadastrarUsuario(usuarioRequestDto)
        );

        //ASSERT
        assertEquals("Email já cadastrado", exception.getMessage());
    }

    @Test
    void listarUsuariosSucesso() {
        Usuario usuario1 = new Usuario("usuario1","usuario1@gmail.com",20);
        Usuario usuario2 = new Usuario("usuario2","usuario2@gmail.com",20);

        when(usuarioRepository.findAll()).thenReturn(List.of(usuario1, usuario2));

        List<UsuarioResponseDto> usuarioResponseDtos = usuarioService.listarUsuarios();

        assertNotNull(usuarioResponseDtos);
        assertEquals(2, usuarioResponseDtos.size());
        assertEquals("usuario1@gmail.com", usuarioResponseDtos.get(0).email());
        assertEquals("usuario2@gmail.com", usuarioResponseDtos.get(1).email());
    }

    @Test
    void buscarUsuarioPorId() {
    }

    @Test
    void atualizarUsuario() {
    }

    @Test
    void deletarUsuario() {
    }
}