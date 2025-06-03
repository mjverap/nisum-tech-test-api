package com.mjvera.nisumtechtest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserEmailNotFoundException extends RuntimeException {
  public UserEmailNotFoundException(String email) {
    super("Usuario con email " + email + " no existe.");
  }
}
