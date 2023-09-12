package lab.space.my_house_24_rest.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import lab.space.my_house_24_rest.entity.Token;
import lab.space.my_house_24_rest.repository.TokenRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogoutServiceTest {
    @Mock
    private TokenRepo tokenRepo;
    @Mock
    private HttpServletRequest request;
    @InjectMocks
    private LogoutService logoutService;

    @Test
    void logout_Unauthorized1() {
        HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);
        logoutService.logout(request,httpServletResponse,any(Authentication.class));
        assertEquals(httpServletResponse.getStatus(),HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    void logout_Unauthorized2() {
        HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        when(tokenRepo.findByToken("token")).thenReturn(Optional.empty());
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer token");
        logoutService.logout(request,httpServletResponse,any(Authentication.class));
        assertEquals(httpServletResponse.getStatus(),HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    void logout_Unauthorized3() {
        HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        when(tokenRepo.findByToken("token")).thenReturn(Optional.of(Token.builder().revoked(true).expired(true).build()));
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer token");
        logoutService.logout(request,httpServletResponse,any(Authentication.class));
        assertEquals(httpServletResponse.getStatus(),HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    void logout() {
        HttpServletResponse httpServletResponse = new MockHttpServletResponse();
        when(tokenRepo.findByToken("token")).thenReturn(Optional.of(Token.builder().revoked(false).expired(false).build()));
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer token");
        logoutService.logout(request,httpServletResponse,any(Authentication.class));
        verify(tokenRepo).save(Token.builder().expired(true).revoked(true).build());
    }


}