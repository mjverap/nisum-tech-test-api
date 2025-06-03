package com.mjvera.nisumtechtest.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mjvera.nisumtechtest.config.TestSecurityConfig;
import com.mjvera.nisumtechtest.controller.UserController;
import com.mjvera.nisumtechtest.dto.PhoneRequestDTO;
import com.mjvera.nisumtechtest.dto.UserRequestDTO;
import com.mjvera.nisumtechtest.service.UserService;
import com.mjvera.nisumtechtest.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(TestSecurityConfig.class)
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void testCreateUser_ValidBody_ReturnsCreated() throws Exception {
        UserRequestDTO validRequest = new UserRequestDTO();
        validRequest.setEmail("test@gmail.com");
        validRequest.setPassword("Password123");
        validRequest.setName("Test User");
        validRequest.setPhones(List.of(
                new PhoneRequestDTO("123456789", "1", "57"),
                new PhoneRequestDTO("987654321", "2", "57")
        ));
        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateUser_InvalidBody_ReturnsBadRequest() throws Exception {
        UserRequestDTO invalidRequest = new UserRequestDTO();

        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateUser_EmptyBody_ReturnsBadRequest() throws Exception {
        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateUser_InvalidPassword_ReturnsBadRequest() throws Exception {
        UserRequestDTO invalidPasswordRequest = new UserRequestDTO();
        invalidPasswordRequest.setEmail("test@gmail.com");
        invalidPasswordRequest.setPassword("short");
        invalidPasswordRequest.setName("Test User");
        invalidPasswordRequest.setPhones(List.of(
                new PhoneRequestDTO("123456789", "1", "57"),
                new PhoneRequestDTO("987654321", "2", "57")
        ));
        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPasswordRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testCreateUser_InvalidEmail_ReturnsBadRequest() throws Exception {
        UserRequestDTO invalidPasswordRequest = new UserRequestDTO();
        invalidPasswordRequest.setEmail("testgmail.com");
        invalidPasswordRequest.setPassword("Password123");
        invalidPasswordRequest.setName("Test User");
        invalidPasswordRequest.setPhones(List.of(
                new PhoneRequestDTO("123456789", "1", "57"),
                new PhoneRequestDTO("987654321", "2", "57")
        ));
        this.mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPasswordRequest)))
                .andExpect(status().isBadRequest());
    }
}