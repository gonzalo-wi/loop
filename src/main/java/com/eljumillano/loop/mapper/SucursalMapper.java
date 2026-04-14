package com.eljumillano.loop.mapper;

import com.eljumillano.loop.dtos.sucursal.CreateSucursalDto;
import com.eljumillano.loop.dtos.sucursal.SucursalDto;
import com.eljumillano.loop.dtos.sucursal.UpdateSucursalDto;
import com.eljumillano.loop.model.Sucursal;
import org.springframework.stereotype.Component;

@Component
public class SucursalMapper {

    public SucursalDto toDto(Sucursal sucursal) {
        SucursalDto dto = new SucursalDto();
        dto.setId(sucursal.getId());
        dto.setName(sucursal.getName());
        dto.setCode(sucursal.getCode());
        dto.setDireccion(sucursal.getDireccion());
        dto.setLocalidad(sucursal.getLocalidad());
        dto.setProvincia(sucursal.getProvincia());
        dto.setCuit(sucursal.getCuit());
        dto.setPackingSlipCode(sucursal.getPackingSlipCode());
        dto.setTotalUsers(sucursal.getUsers() != null ? sucursal.getUsers().size() : 0);
        dto.setCreatedAt(sucursal.getCreatedAt());
        dto.setUpdatedAt(sucursal.getUpdatedAt());
        return dto;
    }

    public Sucursal toEntity(CreateSucursalDto dto) {
        Sucursal sucursal = new Sucursal();
        sucursal.setName(dto.getName());
        sucursal.setCode(dto.getCode());
        sucursal.setDireccion(dto.getDireccion());
        sucursal.setLocalidad(dto.getLocalidad());
        sucursal.setProvincia(dto.getProvincia());
        sucursal.setCuit(dto.getCuit());
        sucursal.setPackingSlipCode(dto.getPackingSlipCode());
        return sucursal;
    }

    public void updateEntity(Sucursal sucursal, UpdateSucursalDto dto) {
        if (dto.getName() != null) {
            sucursal.setName(dto.getName());
        }
        if (dto.getCode() != null) {
            sucursal.setCode(dto.getCode());
        }
        if (dto.getDireccion() != null) {
            sucursal.setDireccion(dto.getDireccion());
        }
        if (dto.getLocalidad() != null) {
            sucursal.setLocalidad(dto.getLocalidad());
        }
        if (dto.getProvincia() != null) {
            sucursal.setProvincia(dto.getProvincia());
        }
        if (dto.getCuit() != null) {
            sucursal.setCuit(dto.getCuit());
        }
        if (dto.getPackingSlipCode() != null) {
            sucursal.setPackingSlipCode(dto.getPackingSlipCode());
        }
    }
}
