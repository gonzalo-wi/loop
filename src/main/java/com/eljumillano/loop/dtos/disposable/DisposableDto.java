package com.eljumillano.loop.dtos.disposable;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class DisposableDto {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String name;

    @Min(value = 0, message = "El orden de visualización no puede ser negativo")
    private Integer displayOrder;

    @Min(value = 1, message = "La cantidad por bulto debe ser mayor a 0")
    private Integer cantBulto;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
