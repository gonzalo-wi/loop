package com.eljumillano.loop.dtos.user;

import com.eljumillano.loop.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
    private Long userId;
    private String name;
    private String lastName;
    private String username;
    private Role role;
    private Long sucursalId;
    private String sucursalName;
    private Long deliveryId;
    private String message;
    private boolean isDelivery;
    
    public LoginResponseDto(Long userId, String name, String lastName, String username, Role role, 
                           Long sucursalId, String sucursalName, Long deliveryId) {
        this.userId = userId;
        this.name = name;
        this.lastName = lastName;
        this.username = username;
        this.role = role;
        this.sucursalId = sucursalId;
        this.sucursalName = sucursalName;
        this.deliveryId = deliveryId;
        this.isDelivery = deliveryId != null;
        this.message = "Login exitoso";
    }
}
