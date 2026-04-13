package com.eljumillano.loop.dtos.sucursal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSucursalDto {
    private String name;
    private String code;
    private String direccion;
    private String localidad;
    private String provincia;
    private String cuit;
}
