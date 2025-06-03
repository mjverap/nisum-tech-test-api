package com.mjvera.nisumtechtest.controller;

import com.mjvera.nisumtechtest.dto.UserRequestDTO;
import com.mjvera.nisumtechtest.dto.UserResponseDTO;
import com.mjvera.nisumtechtest.exception.EmailAlreadyRegisteredException;
import com.mjvera.nisumtechtest.model.User;
import com.mjvera.nisumtechtest.repository.UserRepository;
import com.mjvera.nisumtechtest.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class UserControllerTest {
    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser_Success() {
        UserRequestDTO request = new UserRequestDTO();
        UserResponseDTO response = new UserResponseDTO();
        when(userService.createUser(request)).thenReturn(response);
        ResponseEntity<UserResponseDTO> result = userController.createUser(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    void testCreateUser_EmailAlreadyRegistered() {
        UserRequestDTO request = new UserRequestDTO();
        request.setEmail("test@gmail.com");
        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(new User()));
        when(userService.createUser(request)).thenThrow(new EmailAlreadyRegisteredException("test@gmail.com"));

        assertThrows(EmailAlreadyRegisteredException.class, () -> userController.createUser(request));
    }

    @Test
    void testGetAllUsers_ReturnsList() {
        List<UserResponseDTO> users = List.of(new UserResponseDTO());
        when(userService.getAllUsers()).thenReturn(users);
        ResponseEntity<List<UserResponseDTO>> result = userController.getAllUsers();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(users, result.getBody());
    }

    @Test
    void testGetAllUsers_EmptyList() {
        when(userService.getAllUsers()).thenReturn(List.of());
        ResponseEntity<List<UserResponseDTO>> result = userController.getAllUsers();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(List.of(), result.getBody());
    }
}
