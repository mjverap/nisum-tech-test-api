package com.mjvera.nisumtechtest.controller;

import com.mjvera.nisumtechtest.dto.LoginRequestDTO;
import com.mjvera.nisumtechtest.dto.LoginResponseDTO;
import com.mjvera.nisumtechtest.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class AuthControllerTest {

    @Mock
    private AuthService userService;

//    @Mock
//    private UserRepository userRepository;

    @InjectMocks
    private AuthController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin_Success() {
        Date now = new Date();
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("test@gmail.com");
        request.setPassword("password123");

        LoginResponseDTO response = new LoginResponseDTO("mockedToken", now, now);

        when(userService.login(request)).thenReturn(response);

        ResponseEntity<LoginResponseDTO> result = userController.login(request);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(response, result.getBody());
    }

    @Test
    void testLogin_AuthFails_ThrowsException() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("fail@test.com");
        request.setPassword("wrongPassword");

        when(userService.login(request)).thenThrow(new UsernameNotFoundException("Usuario no encontrado"));

        UsernameNotFoundException thrown = org.junit.jupiter.api.Assertions.assertThrows(
                UsernameNotFoundException.class,
                () -> userController.login(request)
        );

        assertEquals("Usuario no encontrado", thrown.getMessage());
    }

}
