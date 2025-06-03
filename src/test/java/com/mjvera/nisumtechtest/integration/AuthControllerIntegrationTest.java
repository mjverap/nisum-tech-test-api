package com.mjvera.nisumtechtest.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mjvera.nisumtechtest.config.TestSecurityConfig;
import com.mjvera.nisumtechtest.controller.AuthController;
import com.mjvera.nisumtechtest.dto.LoginRequestDTO;
import com.mjvera.nisumtechtest.dto.LoginResponseDTO;
import com.mjvera.nisumtechtest.exception.AuthFailedException;
import com.mjvera.nisumtechtest.service.AuthService;
import com.mjvera.nisumtechtest.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(TestSecurityConfig.class)
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void testLogin_ValidCredentials_ReturnsOk() throws Exception {
        when(authService.login(any(LoginRequestDTO.class))).thenReturn(any(LoginResponseDTO.class));

        LoginRequestDTO validRequest = new LoginRequestDTO();
        validRequest.setEmail("test@gmail.com");
        validRequest.setPassword("Password123");

        this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void testLogin_InvalidCredentials_ReturnsUnauthorized() throws Exception {
        when(authService.login(any(LoginRequestDTO.class))).thenThrow(new AuthFailedException());
        LoginRequestDTO invalidRequest = new LoginRequestDTO();
        invalidRequest.setEmail("test@gmail.com");
        invalidRequest.setPassword("WrongPassword");
        this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testLogin_EmptyBody_ReturnsBadRequest() throws Exception {
        this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLogin_InvalidBody_ReturnsBadRequest() throws Exception {
        LoginRequestDTO invalidRequest = new LoginRequestDTO();
        invalidRequest.setEmail("aaaaaa");
        invalidRequest.setPassword("short");
        this.mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

}