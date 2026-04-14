package com.eljumillano.loop.dtos.order;

import java.time.LocalDateTime;
import java.util.List;

import com.eljumillano.loop.dtos.orderItem.CreateOrderItemDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderDto {

    @NotNull(message = "El ID del delivery es obligatorio")
    private Long deliveryId;

    @NotNull(message = "El ID de la sucursal es obligatorio")
    private Long sucursalId;

    private LocalDateTime orderDate;

    @NotNull(message = "El total es obligatorio")
    @Min(value = 1, message = "El total debe ser mayor a 0")
    private Integer total;

    @NotEmpty(message = "La orden debe tener al menos un item")
    @Valid
    private List<CreateOrderItemDto> items;
}
