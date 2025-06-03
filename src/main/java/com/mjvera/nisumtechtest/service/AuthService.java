package com.mjvera.nisumtechtest.service;

import com.mjvera.nisumtechtest.config.SecurityConfigProps;
import com.mjvera.nisumtechtest.dto.LoginRequestDTO;
import com.mjvera.nisumtechtest.dto.LoginResponseDTO;
import com.mjvera.nisumtechtest.exception.AuthFailedException;
import com.mjvera.nisumtechtest.model.User;
import com.mjvera.nisumtechtest.repository.UserRepository;
import com.mjvera.nisumtechtest.utils.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthService {
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final SecurityConfigProps props;
    private final UserRepository userRepository;

    public AuthService(AuthenticationManager authManager, JwtUtil jwtUtil, SecurityConfigProps props, UserRepository userRepository) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.props = props;
        this.userRepository = userRepository;
    }

    public LoginResponseDTO login(LoginRequestDTO userRequest) {
        UsernamePasswordAuthenticationToken authToken  = new UsernamePasswordAuthenticationToken(userRequest.getEmail(), userRequest.getPassword());
        Authentication authentication = authManager.authenticate(authToken);
        if(!authentication.isAuthenticated()) {
            throw new AuthFailedException();
        }
        String token = generateToken(userRequest.getEmail());
        userRepository.updateLastLoginByEmail(new Date(), userRequest.getEmail());
        return new LoginResponseDTO(
                token,
                jwtUtil.extractIssuedAt(token),
                jwtUtil.extractExpiration(token)
        );
    }

    public String generateToken(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(email));
        if(user.getToken().isEmpty() || !jwtUtil.isTokenValid(user.getToken(), email)) {
            return jwtUtil.generate(email, props.getExpirationTime());
        }else{
            return user.getToken();
        }
    }
}
