package lab.space.my_house_24_rest.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lab.space.my_house_24_rest.config.JwtService;
import lab.space.my_house_24_rest.entity.Token;
import lab.space.my_house_24_rest.entity.User;
import lab.space.my_house_24_rest.enums.TokenType;
import lab.space.my_house_24_rest.mapper.UserMapper;
import lab.space.my_house_24_rest.model.auth.AuthenticationResponse;
import lab.space.my_house_24_rest.model.user.UserResponseForProfile;
import lab.space.my_house_24_rest.repository.TokenRepo;
import lab.space.my_house_24_rest.repository.UserRepository;
import lab.space.my_house_24_rest.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
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
        log.info("Try to get user by id: "+id);
        return userRepository.findById(id).orElseThrow(()->new EntityNotFoundException("User by id:"+id+" not found!"));
    }

    @Override
    public Long getCurrentUser() {
        log.info("Tru ro get current user");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findUserByEmail(authentication.getName()).orElseThrow(()->new EntityNotFoundException("User by email:"+authentication.getName()+" not found!")).getId();
    }

    @Override
    public UserResponseForProfile findUserForProfile() {
        log.info("Try to find user for profile");
        return UserMapper.entityToDtoForProfile(findById(getCurrentUser()));
    }

    @Override
    public ResponseEntity updateEmail(String email) {
        log.info("Try to update email");
        User user = findById(getCurrentUser());
        user.setEmail(email);
        userRepository.save(user);
        log.info("Email update");

        log.info("Try to reload security context");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), user.getAuthorities()));

        log.info("Try to generate new access and refresh token");
        var admin = userRepository.findUserByEmail(user.getEmail()).orElseThrow(()->new EntityNotFoundException("User not found!"));
        var jwtToken = jwtService.generateToken(admin);
        var refreshToken = jwtService.generateRefreshToken(admin);
        revokeAllAdminTokens(admin);
        saveAdminToken(admin,jwtToken);
        return ResponseEntity.ok(AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build());

    }

    private void revokeAllAdminTokens(User user){
        log.info("Try to revoke all user token");
        var validAdminToken = tokenRepo.findAllValidTokensByAdmin(user.getId());
        if(validAdminToken.isEmpty()){
            log.info("User don't have valid token");
            return;
        }
        validAdminToken.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepo.saveAll(validAdminToken);
        log.info("All token revoke");
    }
    private void saveAdminToken(User admin, String jwtToken) {
        log.info("Try to save new user token");
        var token = Token.builder()
                .user(admin)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepo.save(token);
        log.info("Token save");
    }


}
