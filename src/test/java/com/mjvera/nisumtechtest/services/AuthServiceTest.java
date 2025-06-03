package com.mjvera.nisumtechtest.services;

import com.mjvera.nisumtechtest.config.SecurityConfigProps;
import com.mjvera.nisumtechtest.dto.LoginRequestDTO;
import com.mjvera.nisumtechtest.dto.LoginResponseDTO;
import com.mjvera.nisumtechtest.exception.AuthFailedException;
import com.mjvera.nisumtechtest.model.User;
import com.mjvera.nisumtechtest.repository.UserRepository;
import com.mjvera.nisumtechtest.service.AuthService;
import com.mjvera.nisumtechtest.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthServiceTest {

    @Mock
    private AuthenticationManager authManager;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private SecurityConfigProps props;
    @Mock
    private UserRepository userRepository;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin_Success() {
        User user = new User();
        user.setEmail("test@gmail.com");
        user.setToken("mockedToken");
        user.setPassword("password123");

        when(userRepository.findByEmail("test@gmail.com")).thenReturn(java.util.Optional.of(user));
        when(props.getExpirationTime()).thenReturn(3600000L); // 1 hour in milliseconds
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail("test@gmail.com");
        loginRequestDTO.setPassword("password123");

        String token = "mockedToken";
        Date issuedAt = new Date();
        Date expiration = new Date(System.currentTimeMillis() + props.getExpirationTime()); // 1 hour later

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(jwtUtil.generate(loginRequestDTO.getEmail(), props.getExpirationTime())).thenReturn(token);
        when(jwtUtil.extractIssuedAt(token)).thenReturn(issuedAt);
        when(jwtUtil.extractExpiration(token)).thenReturn(expiration);

        LoginResponseDTO response = authService.login(loginRequestDTO);

        assertNotNull(response);
        assertEquals(token, response.getToken());
        assertEquals(issuedAt, response.getIssuedAt());
        assertEquals(expiration, response.getExpiresAt());
        verify(userRepository).updateLastLoginByEmail(any(Date.class), eq(loginRequestDTO.getEmail()));
    }

    @Test
    void testLogin_AuthFailed() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("fail@mail.com");
        request.setPassword("wrong");

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        assertThrows(AuthFailedException.class, () -> authService.login(request));
        verify(userRepository, never()).updateLastLoginByEmail(any(), anyString());
    }
}
