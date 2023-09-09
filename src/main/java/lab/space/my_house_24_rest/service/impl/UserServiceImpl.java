package lab.space.my_house_24_rest.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lab.space.my_house_24_rest.entity.Token;
import lab.space.my_house_24_rest.entity.User;
import lab.space.my_house_24_rest.enums.TokenType;
import lab.space.my_house_24_rest.mapper.UserMapper;
import lab.space.my_house_24_rest.model.auth.AuthenticationRequest;
import lab.space.my_house_24_rest.model.auth.AuthenticationResponse;
import lab.space.my_house_24_rest.model.user.UserResponseForProfile;
import lab.space.my_house_24_rest.repository.TokenRepo;
import lab.space.my_house_24_rest.repository.UserRepository;
import lab.space.my_house_24_rest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TokenRepo tokenRepo;
    private final JwtService jwtService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(username).orElseThrow(()->new EntityNotFoundException("User by email:"+username+" not found"));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.getAuthorities());

    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(()->new EntityNotFoundException("User by id:"+id+" not found!"));
    }

    @Override
    public Long getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findUserByEmail(authentication.getName()).orElseThrow(()->new EntityNotFoundException("User by email:"+authentication.getName()+" not found!")).getId();
    }

    @Override
    public UserResponseForProfile findUserForProfile() {
        return UserMapper.entityToDtoForProfile(findById(getCurrentUser()));
    }

    @Override
    public ResponseEntity updateEmail(String email) {
        User user = findById(getCurrentUser());
        user.setEmail(email);
        userRepository.save(user);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), user.getAuthorities()));

        var admin = userRepository.findUserByEmail(user.getEmail()).orElseThrow(()->new EntityNotFoundException("User not found!"));
        var jwtToken = jwtService.generateToken(admin);
        var refreshToken = jwtService.generateRefreshToken(admin);
        revokeAllAdminTokens(admin);
        saveAdminToken(admin,jwtToken);
        return ResponseEntity.ok(AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build());

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


}
