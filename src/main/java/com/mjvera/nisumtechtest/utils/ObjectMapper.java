package com.mjvera.nisumtechtest.utils;

import com.mjvera.nisumtechtest.dto.PhoneRequestDTO;
import com.mjvera.nisumtechtest.dto.UserRequestDTO;
import com.mjvera.nisumtechtest.dto.UserResponseDTO;
import com.mjvera.nisumtechtest.model.Phone;
import com.mjvera.nisumtechtest.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ObjectMapper {
    public User fromDTOToEntity(UserRequestDTO userRequestDTO) {
        User user = new User();
            user.setName(userRequestDTO.getName());
            user.setEmail(userRequestDTO.getEmail());
            user.setPhones(fromDTOToPhones(userRequestDTO.getPhones()));
        return user;
    }

    public UserResponseDTO fromEntityToDTO(User user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setName(user.getName());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setCreatedAt(user.getCreated());
        userResponseDTO.setModifiedAt(user.getModified());
        userResponseDTO.setLastLogin(user.getLastLogin());
        userResponseDTO.setToken(user.getToken());
        userResponseDTO.setActive(user.isActive());
        return userResponseDTO;
    }

    public List<Phone> fromDTOToPhones(List<PhoneRequestDTO> requestDTOS) {
        return requestDTOS.stream()
                .map(phone -> new Phone(phone.getNumber(), phone.getCityCode(), phone.getCountryCode()))
                .toList();
    }
}
