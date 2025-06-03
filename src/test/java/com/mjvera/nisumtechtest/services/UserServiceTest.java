package com.mjvera.nisumtechtest.services;

import com.mjvera.nisumtechtest.config.SecurityConfigProps;
import com.mjvera.nisumtechtest.dto.PhoneRequestDTO;
import com.mjvera.nisumtechtest.dto.UserRequestDTO;
import com.mjvera.nisumtechtest.dto.UserResponseDTO;
import com.mjvera.nisumtechtest.exception.EmailAlreadyRegisteredException;
import com.mjvera.nisumtechtest.model.Phone;
import com.mjvera.nisumtechtest.model.User;
import com.mjvera.nisumtechtest.repository.UserRepository;
import com.mjvera.nisumtechtest.service.UserService;
import com.mjvera.nisumtechtest.utils.JwtUtil;
import com.mjvera.nisumtechtest.utils.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private SecurityConfigProps props;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser_Success() {
        UserRequestDTO request = new UserRequestDTO();
        request.setEmail("test@gmail.com");
        request.setPassword("123456");
        request.setName("Test McTesterson");
        request.setPhones(java.util.List.of(
                new PhoneRequestDTO("123456789", "1", "57"),
                new PhoneRequestDTO("987654321", "2", "57")
        ));

        User userEntity = new User();
        userEntity.setEmail("test@gmail.com");

        User savedUser = new User();
        savedUser.setEmail("test@gmail.com");
        savedUser.setPassword("encoded");
        savedUser.setToken("token");
        savedUser.setCreated(new Date());
        savedUser.setModified(new Date());
        savedUser.setLastLogin(new Date());
        savedUser.setActive(true);

        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setEmail("test@gmail.com");
        responseDTO.setToken("token");

        when(userRepository.findByEmail("test@gmail.com")).thenReturn(Optional.empty());
        when(objectMapper.fromDTOToEntity(request)).thenReturn(userEntity);
        when(passwordEncoder.encode("123456")).thenReturn("encoded");
        when(jwtUtil.generate(anyString(), anyLong())).thenReturn("token");
        when(props.getExpirationTime()).thenReturn(3600L);
        when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(savedUser);
        when(objectMapper.fromEntityToDTO(savedUser)).thenReturn(responseDTO);

        UserResponseDTO result = userService.createUser(request);

        assertNotNull(result);
        assertEquals("test@gmail.com", result.getEmail());
        assertEquals("token", result.getToken());
    }

    @Test
    void testCreateUser_EmailAlreadyRegistered() {
        UserRequestDTO request = new UserRequestDTO();
        request.setEmail("test@mail.com");
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(new User()));
        assertThrows(EmailAlreadyRegisteredException.class, () -> userService.createUser(request));
    }
}
