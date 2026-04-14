package com.eljumillano.loop.dtos.sucursal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SucursalDto {
    private Long id;
    private String name;
    private String code;
    private String direccion;
    private String localidad;
    private String provincia;
    private String cuit;
    private String packingSlipCode;  // Código para packing slips/remitos
    private Integer totalUsers;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
