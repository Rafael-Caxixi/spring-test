package com.rafaelcaxixi.spring_junit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafaelcaxixi.spring_junit.dtos.AtualizarSenhaRequestDTO;
import com.rafaelcaxixi.spring_junit.dtos.LoginRequestDTO;
import com.rafaelcaxixi.spring_junit.dtos.LoginResponseDTO;
import com.rafaelcaxixi.spring_junit.exceptions.UnauthorizedException;
import com.rafaelcaxixi.spring_junit.repositories.UsuarioRepository;
import com.rafaelcaxixi.spring_junit.services.AuthService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private AuthController authController;

    @Mock
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private LoginRequestDTO loginRequestDTO;

    @BeforeEach
    void setUp() {
        authService = Mockito.mock(AuthService.class);
        authController = new AuthController(authService);
    }

    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    // 2️⃣ Sobrescreve propriedades do Spring para usar o container
    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Test
    void deveRealizarLoginComCredenciaisValidas() {
        LoginRequestDTO request = new LoginRequestDTO("usuario", "senha123");
        LoginResponseDTO responseDTO = new LoginResponseDTO("tokenJWT");

        when(authService.login(request)).thenReturn(responseDTO);

        ResponseEntity<LoginResponseDTO> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());
    }

    @Test
    void deveRetornar401AoRealizarLoginComCredenciaisInvalidas() {
        LoginRequestDTO request = new LoginRequestDTO("usuario", "senhaErrada");

        when(authService.login(request)).thenThrow(new BadCredentialsException("Credenciais inválidas"));

        assertThrows(BadCredentialsException.class, () -> authController.login(request));
    }

    @Test
    void deveAtualizarSenhaComSucesso() {
        AtualizarSenhaRequestDTO dto = new AtualizarSenhaRequestDTO("senhaAtual", "novaSenha");
        Authentication auth = new UsernamePasswordAuthenticationToken("joaolima", null);

        doNothing().when(authService).atualizarSenha(dto, 1L, auth);

        ResponseEntity<Void> response = authController.atualizarSenha(dto, 1L, auth);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deveRetornar401AoAtualizarComSenhaAtualIncorreta() {
        AtualizarSenhaRequestDTO dto = new AtualizarSenhaRequestDTO("senhaErrada", "novaSenha");
        Authentication auth = new UsernamePasswordAuthenticationToken("joaolima", null);

        doThrow(new UnauthorizedException("Senha atual incorreta"))
                .when(authService).atualizarSenha(dto, 1L, auth);

        assertThrows(UnauthorizedException.class, () -> authController.atualizarSenha(dto, 1L, auth));
    }
}
