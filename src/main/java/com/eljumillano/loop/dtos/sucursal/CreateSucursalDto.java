package com.eljumillano.loop.dtos.sucursal;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateSucursalDto {
    
    @NotBlank(message = "El nombre es obligatorio")
    private String name;
    
    @NotBlank(message = "El código es obligatorio")
    private String code;
    
    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;
    
    @NotBlank(message = "La localidad es obligatoria")
    private String localidad;
    
    @NotBlank(message = "La provincia es obligatoria")
    private String provincia;
    
    private String cuit;
}
