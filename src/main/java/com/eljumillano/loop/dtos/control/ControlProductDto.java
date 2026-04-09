package com.eljumillano.loop.dtos.control;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ControlProductDto {

    private Long id;

    @NotNull(message = "El producto es obligatorio")
    private Long productId;

    private String productName;

    @Min(value = 0, message = "El conteo de llenos no puede ser negativo")
    private int fullCount;

    @Min(value = 0, message = "El conteo total no puede ser negativo")
    private int totalCount;

    @Min(value = 0, message = "Los recambios no pueden ser negativos")
    private int replacements;
}
