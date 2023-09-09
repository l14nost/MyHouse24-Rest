package lab.space.my_house_24_rest.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lab.space.my_house_24_rest.model.auth.AuthenticationRequest;
import lab.space.my_house_24_rest.model.auth.AuthenticationResponse;
import lab.space.my_house_24_rest.entity.Token;
import lab.space.my_house_24_rest.entity.User;
import lab.space.my_house_24_rest.enums.TokenType;
import lab.space.my_house_24_rest.repository.TokenRepo;
import lab.space.my_house_24_rest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepo tokenRepo;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    private void saveAdminToken(User admin, String jwtToken) {
        var token = Token.builder()
                .user(admin)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepo.save(token);
    }



    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getLogin(),
                        request.getPassword()
                )
        );

        var admin = userRepository.findUserByEmail(request.getLogin()).orElseThrow(()->new EntityNotFoundException("User not found!"));
        var jwtToken = jwtService.generateToken(admin);
        var refreshToken = jwtService.generateRefreshToken(admin);
        revokeAllAdminTokens(admin);
        saveAdminToken(admin,jwtToken);
        return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
    }

    private void revokeAllAdminTokens(User user){
        var validAdminToken = tokenRepo.findAllValidTokensByAdmin(user.getId());
        if(validAdminToken.isEmpty()){
            return;
        }
        validAdminToken.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepo.saveAll(validAdminToken);
    }


    public ResponseEntity refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String adminLogin;
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
        }
        refreshToken = authHeader.substring(7);
        adminLogin = jwtService.extractUsername(refreshToken);
        if(adminLogin!=null ){
            var user = this.userRepository.findUserByEmail(adminLogin).orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user) ){
                var accessToken = jwtService.generateToken(user);
                revokeAllAdminTokens(user);
                saveAdminToken(user,accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                return ResponseEntity.ok(authResponse);
            }
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("UNAUTHORIZED");
    }
}
