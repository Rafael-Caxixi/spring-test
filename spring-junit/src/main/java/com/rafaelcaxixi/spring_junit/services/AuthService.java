package com.rafaelcaxixi.spring_junit.services;

import com.rafaelcaxixi.spring_junit.domains.Usuario;
import com.rafaelcaxixi.spring_junit.dtos.AtualizarSenhaRequestDTO;
import com.rafaelcaxixi.spring_junit.dtos.LoginRequestDTO;
import com.rafaelcaxixi.spring_junit.dtos.LoginResponseDTO;
import com.rafaelcaxixi.spring_junit.dtos.UsuarioResponseDto;
import com.rafaelcaxixi.spring_junit.exceptions.ResourceNotFoundException;
import com.rafaelcaxixi.spring_junit.exceptions.UnauthorizedException;
import com.rafaelcaxixi.spring_junit.repositories.UsuarioRepository;
import com.rafaelcaxixi.spring_junit.security.services.PasswordService;
import com.rafaelcaxixi.spring_junit.security.services.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final TokenService tokenService;
    private final PasswordService passwordService;


    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO){
        if (!usuarioRepository.existsByLogin(loginRequestDTO.login())) {
            throw new ResourceNotFoundException("Usuário com login '" + loginRequestDTO.login() + "' não encontrado.");
        }

        try  {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.login(),
                            loginRequestDTO.senha()
                    )
            );
            Usuario user = (Usuario) authentication.getPrincipal();
            return new LoginResponseDTO(tokenService.generateToken(user.getLogin()));

        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Senha incorreta.");
        }
    }

    public void atualizarSenha(AtualizarSenhaRequestDTO atualizarSenhaRequestDTO,Long id, Authentication authentication){

        Usuario usuarioToken = (Usuario) authentication.getPrincipal();

        if (!usuarioToken.getId().equals(id)) {
            throw new UnauthorizedException("Você não tem permissão para alterar a senha de outro usuário.");
        }

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com id '" + id + "' não encontrado."));

        if (!passwordService.matches(atualizarSenhaRequestDTO.senhaAtual(), usuario.getSenha())) {
            throw new BadCredentialsException("Senha atual incorreta.");
        }

        usuario.atualizarSenha(passwordService.encryptPassword(atualizarSenhaRequestDTO.novaSenha()));
        usuarioRepository.save(usuario);
    }

}
