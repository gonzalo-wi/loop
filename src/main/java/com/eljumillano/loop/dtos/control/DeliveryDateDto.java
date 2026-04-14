package com.eljumillano.loop.dtos.control;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDateDto {

    @NotNull(message = "La fecha de entrega es obligatoria")
    private LocalDate deliveryDate;
}
