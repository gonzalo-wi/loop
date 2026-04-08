package com.eljumillano.loop.dtos.product;
import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {

    private Long id;

    @Min(value = 0, message = "El orden no puede ser negativo")
    private int order;

    @NotBlank(message = "El código de producto es obligatorio")
    @Size(max = 50, message = "El código no puede superar los 50 caracteres")
    private String codProduct;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String name;

    @Min(value = 1, message = "El paquete debe ser mayor a 0")
    private int pack;

    private boolean returnable;

    @NotNull(message = "El control es obligatorio")
    private Long controlId;

    private LocalDateTime createdAt;
}
