package com.mjvera.nisumtechtest.service;

import com.mjvera.nisumtechtest.config.SecurityConfigProps;
import com.mjvera.nisumtechtest.dto.UserRequestDTO;
import com.mjvera.nisumtechtest.dto.UserResponseDTO;
import com.mjvera.nisumtechtest.exception.EmailAlreadyRegisteredException;
import com.mjvera.nisumtechtest.model.User;
import com.mjvera.nisumtechtest.repository.UserRepository;
import com.mjvera.nisumtechtest.utils.JwtUtil;
import com.mjvera.nisumtechtest.utils.ObjectMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository repository;
    private final ObjectMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final SecurityConfigProps props;

    public UserService(
            UserRepository repository, ObjectMapper mapper, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, SecurityConfigProps props) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.props = props;
    }

    public UserResponseDTO createUser(UserRequestDTO user) {
        if (repository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailAlreadyRegisteredException(user.getEmail());
        }
        User entity = mapper.fromDTOToEntity(user);
        Date now = new Date();
        entity.setPassword(passwordEncoder.encode(user.getPassword()));
        entity.setToken(generateToken(entity));
        entity.setCreated(now);
        entity.setModified(now);
        entity.setLastLogin(now);
        entity.setActive(true);
        User savedUser = repository.save(entity);
        return mapper.fromEntityToDTO(savedUser);
    }

    public List<UserResponseDTO> getAllUsers() {
        return repository.findAll().stream()
                .map(mapper::fromEntityToDTO)
                .collect(Collectors.toList());
    }

    public String generateToken(User user) {
        return jwtUtil.generate(user.getEmail(), props.getExpirationTime());
    }
}
