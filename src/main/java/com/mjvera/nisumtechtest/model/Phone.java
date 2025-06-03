package com.mjvera.nisumtechtest.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "phones")
@Data
@NoArgsConstructor
public class Phone {
    @Id
    @UuidGenerator
    private UUID id;
    private String number;
    private String citycode;
    private String countrycode;

    public Phone(String number, String citycode, String countrycode) {
        this.number = number;
        this.citycode = citycode;
        this.countrycode = countrycode;
    }
}
