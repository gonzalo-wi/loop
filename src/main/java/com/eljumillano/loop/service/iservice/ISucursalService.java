package com.eljumillano.loop.service.iservice;

import com.eljumillano.loop.dtos.sucursal.CreateSucursalDto;
import com.eljumillano.loop.dtos.sucursal.SucursalDto;
import com.eljumillano.loop.dtos.sucursal.UpdateSucursalDto;

import java.util.List;

public interface ISucursalService {
    
    SucursalDto createSucursal(CreateSucursalDto createSucursalDto);
    
    SucursalDto getSucursalById(Long id);
    
    List<SucursalDto> getAllSucursales();
    
    SucursalDto getSucursalByCode(String code);
    
    List<SucursalDto> getSucursalesByLocalidad(String localidad);
    
    List<SucursalDto> getSucursalesByProvincia(String provincia);
    
    SucursalDto updateSucursal(Long id, UpdateSucursalDto updateSucursalDto);
    
    void deleteSucursal(Long id);
    
    boolean existsByCode(String code);
}
