package lab.space.my_house_24_rest.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lab.space.my_house_24_rest.config.JwtService;
import lab.space.my_house_24_rest.entity.Token;
import lab.space.my_house_24_rest.entity.User;
import lab.space.my_house_24_rest.model.auth.AuthenticationRequest;
import lab.space.my_house_24_rest.model.auth.AuthenticationResponse;
import lab.space.my_house_24_rest.repository.TokenRepo;
import lab.space.my_house_24_rest.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private TokenRepo tokenRepo;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void authenticate() {
        User user = User.builder().id(1L).build();
        when(userRepository.findUserByEmail("test@gmail.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("token");
        when(jwtService.generateRefreshToken(user)).thenReturn("token1");
        when(tokenRepo.findAllValidTokensByAdmin(1L)).thenReturn(List.of());
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(AuthenticationRequest.builder().login("test@gmail.com").password("pass").build());
        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(
                "test@gmail.com",
                "pass"
        ));
        verify(tokenRepo).save(any(Token.class));
        verify(tokenRepo,times(0)).saveAll(List.of(Token.builder().build()));
        assertEquals(AuthenticationResponse.builder()
                .accessToken("token")
                .refreshToken("token1")
                .build(), authenticationResponse);


    }

    @Test
    void refreshToken_Unauthorized1() throws IOException {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);
        ResponseEntity responseEntity = authenticationService.refreshToken(request,response);
        assertEquals(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED"), responseEntity);

    }

    @Test
    void refreshToken_Unauthorized2() throws IOException {
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer token");
        ResponseEntity responseEntity = authenticationService.refreshToken(request,response);
        assertEquals(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED"), responseEntity);

    }

    @Test
    void refreshToken() throws IOException {
        User user = User.builder().id(1L).build();
        List<Token> tokenList = List.of(Token.builder().build(),
                Token.builder().build());
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer token");
        when(jwtService.extractUsername("token")).thenReturn("test");
        when(userRepository.findUserByEmail("test")).thenReturn(Optional.of(user));
        when((jwtService.isTokenValid("token", user))).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("token1");
        when(tokenRepo.findAllValidTokensByAdmin(1L)).thenReturn(tokenList);
        ResponseEntity responseEntity = authenticationService.refreshToken(request,response);

        assertEquals(ResponseEntity.ok(AuthenticationResponse.builder()
                .refreshToken("token")
                .accessToken("token1")
                .build()), responseEntity);

    }
}