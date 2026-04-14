package com.eljumillano.loop.mapper;

import org.springframework.stereotype.Component;

import com.eljumillano.loop.dtos.packingslip.PackingSlipDto;
import com.eljumillano.loop.model.PackingSlip;

@Component
public class PackingSlipMapper {

    private final ControlMapper controlMapper;

    public PackingSlipMapper(ControlMapper controlMapper) {
        this.controlMapper = controlMapper;
    }

    public PackingSlipDto toDto(PackingSlip packingSlip) {
        if (packingSlip == null) {
            return null;
        }
        
        PackingSlipDto dto = new PackingSlipDto();
        dto.setId(packingSlip.getId());
        dto.setSlipNumber(packingSlip.getSlipNumber());
        dto.setControlId(packingSlip.getControl() != null ? packingSlip.getControl().getId() : null);
        dto.setOrderId(packingSlip.getOrder() != null ? packingSlip.getOrder().getId() : null);
        dto.setDeliveryId(packingSlip.getDeliveryId());
        dto.setSupervisorId(packingSlip.getSupervisorId());
        dto.setSucursalId(packingSlip.getSucursalId());
        dto.setStatus(packingSlip.getStatus());
        dto.setCreatedAt(packingSlip.getCreatedAt());
        dto.setObservations(packingSlip.getObservations());
        
        // Productos del control
        if (packingSlip.getControl() != null && packingSlip.getControl().getProducts() != null) {
            dto.setProducts(packingSlip.getControl().getProducts().stream()
                    .map(cp -> {
                        var productDto = new com.eljumillano.loop.dtos.control.ControlProductDto();
                        productDto.setId(cp.getId());
                        productDto.setProductId(cp.getProduct().getId());
                        productDto.setProductName(cp.getProduct().getName());
                        productDto.setFullCount(cp.getFullCount());
                        productDto.setTotalCount(cp.getTotalCount());
                        productDto.setReplacements(cp.getReplacements());
                        return productDto;
                    })
                    .toList());
        }
        
        return dto;
    }
}
