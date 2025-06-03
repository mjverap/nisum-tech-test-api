package com.mjvera.nisumtechtest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class NisumTechTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(NisumTechTestApplication.class, args);
    }

}
