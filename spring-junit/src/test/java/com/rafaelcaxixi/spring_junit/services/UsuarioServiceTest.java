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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private UsuarioRequestDto usuarioRequestDto;

    @BeforeEach
    void setUp() {
        usuarioRequestDto = new UsuarioRequestDto("Rafael Caxixi", "usuarioteste@gmail.com",20,"123456");
    }


    @Test
    void cadastrarUsuarioComSucesso() {
        //ARRANGE
        Usuario usuario = new Usuario("Rafael Caxixi","usuarioteste@gmail.com",20,"123456");
        usuario.setId(1L);
        when(usuarioRepository.existsByLogin(usuarioRequestDto.login())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);


        //ACT
        UsuarioResponseDto usuarioResponseDto = usuarioService.cadastrarUsuario(usuarioRequestDto);

        //ASSERT
        assertNotNull(usuarioResponseDto);
        assertEquals("usuarioteste@gmail.com", usuarioResponseDto.email());
        then(usuarioRepository).should(times(1)).save(any(Usuario.class));
    }

    @Test
    void cadastrarUsuarioComLoginExistente() {
        //ARRANGE + ACT
        when(usuarioRepository.existsByLogin(usuarioRequestDto.login())).thenReturn(true);
        DataIntegrityViolationException exception = assertThrows(
                DataIntegrityViolationException.class,
                () -> usuarioService.cadastrarUsuario(usuarioRequestDto)
        );

        //ASSERT
        assertEquals("Login já cadastrado", exception.getMessage());
    }

    @Test
    void listarUsuariosSucesso() {
        Usuario usuario1 = new Usuario("usuario1","usuario1@gmail.com",20,"123456");
        Usuario usuario2 = new Usuario("usuario2","usuario2@gmail.com",20,"123456");

        when(usuarioRepository.findAll()).thenReturn(List.of(usuario1, usuario2));

        List<UsuarioResponseDto> usuarioResponseDtos = usuarioService.listarUsuarios();

        assertNotNull(usuarioResponseDtos);
        assertEquals(2, usuarioResponseDtos.size());
        assertEquals("usuario1@gmail.com", usuarioResponseDtos.get(0).email());
        assertEquals("usuario2@gmail.com", usuarioResponseDtos.get(1).email());
    }

    @Test
    void buscarUsuarioPorIdComSucesso() {
        //ARRANGE
        Usuario usuario = new Usuario("Nome","email@gmail.com",20,"123456");
        usuario.setId(1L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        //ACT
        UsuarioResponseDto usuarioResponseDto = usuarioService.buscarUsuarioPorId(1L);

        //ASSERT
        assertNotNull(usuarioResponseDto);
        assertEquals("email@gmail.com", usuarioResponseDto.email());
        assertEquals(1L, usuarioResponseDto.id());
    }

    @Test
    void buscarUsuarioPorIdComUsuarioInexistente() {
        //ARRANGE
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        //ACT
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> usuarioService.buscarUsuarioPorId(1L));

        //ASSERT
        assertEquals("Usuário não encontrado", exception.getMessage());
    }

    @Test
    void atualizarUsuarioComSucesso() {
        //ARRANGE
        Usuario usuario = new Usuario("Nome","email@gmail.com",20,"123456");
        usuario.setId(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        //ACT
        usuarioService.atualizarUsuario(1L, usuarioRequestDto);

        //ASSERT
        then(usuarioRepository).should(times(1)).save(any(Usuario.class));
        assertEquals("usuarioteste@gmail.com", usuario.getEmail());
    }

    @Test
    void deletarUsuarioComSucesso() {
        //ARRANGE
        Usuario usuario = new Usuario("Nome","email@gmail.com",20,"123456");
        usuario.setId(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepository).delete(usuario);

        //ACT
        usuarioService.deletarUsuario(1L);

        //ASSERT
        verify(usuarioRepository,times(1)).delete(usuario);
    }
}