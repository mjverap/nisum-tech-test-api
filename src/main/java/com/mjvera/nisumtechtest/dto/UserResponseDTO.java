package com.mjvera.nisumtechtest.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class UserResponseDTO {
    private UUID id;
    private String name;
    private String email;
    private Date createdAt;
    private Date modifiedAt;
    private Date lastLogin;
    private String token;
    private boolean isActive;
}
