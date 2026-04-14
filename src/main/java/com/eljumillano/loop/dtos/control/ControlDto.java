package com.eljumillano.loop.dtos.control;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.eljumillano.loop.model.enums.TypeControl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ControlDto {

    private Long id;

    @NotNull(message = "El delivery es obligatorio")
    private Long deliveryId;

    @NotNull(message = "El supervisor es obligatorio")
    private Long supervisorId;

    @NotNull(message = "La sucursal es obligatoria")
    private Long sucursalId;

    @NotNull(message = "El tipo de control es obligatorio")
    private TypeControl typeControl;

    private LocalDate deliveryDate;

    private Boolean orderly;
    private Boolean checked;

    @Valid
    private List<ControlProductDto> products;

    private LocalDateTime createdAt;
}
