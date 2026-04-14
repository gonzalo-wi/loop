package com.eljumillano.loop.dtos.order;

import com.eljumillano.loop.model.enums.OrderState;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderStateDto {
    
    @NotNull(message = "El estado es obligatorio")
    private OrderState state;
}
