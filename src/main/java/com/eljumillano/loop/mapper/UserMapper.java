package com.eljumillano.loop.mapper;

import com.eljumillano.loop.dtos.user.LoginResponseDto;
import com.eljumillano.loop.dtos.user.RegisterDto;
import com.eljumillano.loop.dtos.user.UserDto;
import com.eljumillano.loop.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setLastName(user.getLastName());
        dto.setUsername(user.getUsername());
        dto.setPin(user.getPin());
        dto.setSucursalId(user.getSucursal().getId());
        dto.setSucursalName(user.getSucursal().getName());
        dto.setRole(user.getRole());
        dto.setDeliveryId(user.getDeliveryId());
        dto.setActive(user.isActive());
        return dto;
    }

    public User toEntity(RegisterDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setLastName(dto.getLastName());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setPin(dto.getPin());
        user.setRole(dto.getRole());
        user.setDeliveryId(dto.getDeliveryId());
        user.setActive(true);
        return user;
    }

    public LoginResponseDto toLoginResponse(User user) {
        return new LoginResponseDto(
            user.getId(),
            user.getName(),
            user.getLastName(),
            user.getUsername(),
            user.getRole(),
            user.getSucursal().getId(),
            user.getSucursal().getName(),
            user.getDeliveryId()
        );
    }
}
