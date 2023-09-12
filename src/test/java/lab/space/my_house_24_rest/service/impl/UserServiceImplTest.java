package lab.space.my_house_24_rest.service.impl;

import lab.space.my_house_24_rest.config.JwtService;
import lab.space.my_house_24_rest.entity.Token;
import lab.space.my_house_24_rest.entity.User;
import lab.space.my_house_24_rest.enums.UserStatus;
import lab.space.my_house_24_rest.model.auth.AuthenticationResponse;
import lab.space.my_house_24_rest.model.user.UserResponseForProfile;
import lab.space.my_house_24_rest.repository.TokenRepo;
import lab.space.my_house_24_rest.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private TokenRepo tokenRepo;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void loadUserByUsername() {
        when(userRepository.findUserByEmail("test@gmail.com")).thenReturn(Optional.of(User.builder().email("test@gmail.com").password("pass").userStatus(UserStatus.ACTIVE).build()));
        UserDetails userDetails = userService.loadUserByUsername("test@gmail.com");
        assertEquals(userDetails.getUsername(),"test@gmail.com");
        assertEquals(userDetails.getPassword(),"pass");
        assertEquals(userDetails.getAuthorities().size(), 1);
    }

    @Test
    void findById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(User.builder().id(1L).email("test@gmail.com").password("pass").userStatus(UserStatus.ACTIVE).build()));
        User user = userService.findById(1L);
        assertEquals(User.builder().id(1L).email("test@gmail.com").password("pass").userStatus(UserStatus.ACTIVE).build(),user);
    }

    @Test
    void getCurrentUser() {
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("mail@gmail.com");
        when(userRepository.findUserByEmail("mail@gmail.com")).thenReturn(Optional.of(User.builder().id(1L).build()));
        Long id = userService.getCurrentUser();
        assertEquals(1L, id);

    }

    @Test
    void findUserForProfile() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(User.builder().id(1L).apartmentList(new ArrayList<>()).build()));
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("mail@gmail.com");
        when(userRepository.findUserByEmail("mail@gmail.com")).thenReturn(Optional.of(User.builder().id(1L).apartmentList(new ArrayList<>()).build()));
        UserResponseForProfile userResponseForProfile = userService.findUserForProfile();
        assertEquals(UserResponseForProfile.builder().fullName("null null null").id(1L).apartments(new ArrayList<>()).build(),userResponseForProfile);
    }

    @Test
    void updateEmail() {
        User user = User.builder().id(1L).apartmentList(new ArrayList<>()).userStatus(UserStatus.ACTIVE).build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("mail@gmail.com");
        when(userRepository.findUserByEmail("mail@gmail.com")).thenReturn(Optional.of(user));

        when(jwtService.generateToken(user)).thenReturn("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbWlyYmFub3JAZ21haWwuY29tIiwiaWF0IjoxNjk0MTcwMzQyLCJleHAiOjE2OTQyNTY3NDJ9.wtECnKQU4wqZ8AhRxEwWtUANT_GX4F1a6ZTKD2yBufk");

        when(jwtService.generateRefreshToken(user)).thenReturn("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbWlyYmFub3JAZ21haWwuY29tIiwiaWF0IjoxNjk0MTcwMzQyLCJleHAiOjE2OTQyNTY3NDJ9.wtECnKQU4wqZ8AhRxEwWtUANT_GX4F1a6ZTKD2yBufk");

        when(tokenRepo.findAllValidTokensByAdmin(1L)).thenReturn(new ArrayList<>());

        ResponseEntity responseEntity = userService.updateEmail("mail@gmail.com");
        assertEquals(ResponseEntity.ok(
                AuthenticationResponse.builder()
                        .accessToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbWlyYmFub3JAZ21haWwuY29tIiwiaWF0IjoxNjk0MTcwMzQyLCJleHAiOjE2OTQyNTY3NDJ9.wtECnKQU4wqZ8AhRxEwWtUANT_GX4F1a6ZTKD2yBufk")
                        .refreshToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbWlyYmFub3JAZ21haWwuY29tIiwiaWF0IjoxNjk0MTcwMzQyLCJleHAiOjE2OTQyNTY3NDJ9.wtECnKQU4wqZ8AhRxEwWtUANT_GX4F1a6ZTKD2yBufk")
                        .build()

                ),responseEntity);

    }


    @Test
    void updateEmail_revokeAll() {
        User user = User.builder().id(1L).apartmentList(new ArrayList<>()).userStatus(UserStatus.ACTIVE).build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("mail@gmail.com");
        when(userRepository.findUserByEmail("mail@gmail.com")).thenReturn(Optional.of(user));

        when(jwtService.generateToken(user)).thenReturn("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbWlyYmFub3JAZ21haWwuY29tIiwiaWF0IjoxNjk0MTcwMzQyLCJleHAiOjE2OTQyNTY3NDJ9.wtECnKQU4wqZ8AhRxEwWtUANT_GX4F1a6ZTKD2yBufk");

        when(jwtService.generateRefreshToken(user)).thenReturn("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbWlyYmFub3JAZ21haWwuY29tIiwiaWF0IjoxNjk0MTcwMzQyLCJleHAiOjE2OTQyNTY3NDJ9.wtECnKQU4wqZ8AhRxEwWtUANT_GX4F1a6ZTKD2yBufk");

        when(tokenRepo.findAllValidTokensByAdmin(1L)).thenReturn(List.of(Token.builder().build(),
                Token.builder().build()));

        ResponseEntity responseEntity = userService.updateEmail("mail@gmail.com");
        assertEquals(ResponseEntity.ok(
                AuthenticationResponse.builder()
                        .accessToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbWlyYmFub3JAZ21haWwuY29tIiwiaWF0IjoxNjk0MTcwMzQyLCJleHAiOjE2OTQyNTY3NDJ9.wtECnKQU4wqZ8AhRxEwWtUANT_GX4F1a6ZTKD2yBufk")
                        .refreshToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbWlyYmFub3JAZ21haWwuY29tIiwiaWF0IjoxNjk0MTcwMzQyLCJleHAiOjE2OTQyNTY3NDJ9.wtECnKQU4wqZ8AhRxEwWtUANT_GX4F1a6ZTKD2yBufk")
                        .build()

        ),responseEntity);

    }
}