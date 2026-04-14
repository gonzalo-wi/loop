package com.eljumillano.loop.dtos.packingslip;

import java.time.LocalDateTime;
import java.util.List;

import com.eljumillano.loop.dtos.control.ControlProductDto;
import com.eljumillano.loop.model.enums.PackingSlipStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PackingSlipDto {

    private Long id;
    private String slipNumber;
    private Long controlId;
    private Long orderId;
    private Long deliveryId;
    private String deliveryName;  // Para mostrar en el PDF
    private Long supervisorId;
    private String supervisorName;  // Para mostrar en el PDF
    private Long sucursalId;
    private String sucursalName;  // Para mostrar en el PDF
    private PackingSlipStatus status;
    private LocalDateTime createdAt;
    private String observations;
    
    // Productos del control
    private List<ControlProductDto> products;
}
