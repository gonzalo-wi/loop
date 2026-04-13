package com.eljumillano.loop.dtos.user;

import com.eljumillano.loop.model.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    
    @NotBlank(message = "El nombre es obligatorio")
    private String name;
    
    private String lastName;
    
    private String username;
    
    private String password;
    
    private Integer pin;
    
    @NotNull(message = "La sucursal es obligatoria")
    private Long sucursalId;
    
    @NotNull(message = "El rol es obligatorio")
    private Role role;
    
    private Long deliveryId;
}
