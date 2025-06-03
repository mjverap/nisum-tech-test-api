package com.mjvera.nisumtechtest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class LoginResponseDTO {
    private final String token;
    private final Date issuedAt;
    private final Date expiresAt;
}
