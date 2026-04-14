package com.eljumillano.loop.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eljumillano.loop.dtos.sucursal.CreateSucursalDto;
import com.eljumillano.loop.dtos.sucursal.SucursalDto;
import com.eljumillano.loop.dtos.sucursal.UpdateSucursalDto;
import com.eljumillano.loop.exception.ResourceNotFoundException;
import com.eljumillano.loop.mapper.SucursalMapper;
import com.eljumillano.loop.model.Sucursal;
import com.eljumillano.loop.repository.SucursalRepository;
import com.eljumillano.loop.service.iservice.ISucursalService;

@Service
public class SucursalService implements ISucursalService {

    private static final String SUCURSAL_DUPLICADA            = "Ya existe una sucursal con el código: ";
    private static final String SUCURSAL_NO_ENCONTRADA_ID     = "Sucursal no encontrada con id: ";
    private static final String SUCURSAL_NO_ENCONTRADA_CODIGO = "Sucursal no encontrada con código: ";
    private static final String SUCURSAL_CON_USUARIOS         = "No se puede eliminar la sucursal porque tiene %d usuarios asignados";
    
    @Autowired
    private SucursalRepository sucursalRepository;

    @Autowired
    private SucursalMapper sucursalMapper;

    @Override
    @Transactional
    public SucursalDto createSucursal(CreateSucursalDto createSucursalDto) {
        if (sucursalRepository.findByCode(createSucursalDto.getCode()).isPresent()) {
            throw new IllegalArgumentException(SUCURSAL_DUPLICADA + createSucursalDto.getCode());
        }
        var sucursal      = sucursalMapper.toEntity(createSucursalDto);
        var savedSucursal = sucursalRepository.save(sucursal);
        return sucursalMapper.toDto(savedSucursal);
    }


    @Override
    @Transactional(readOnly = true)
    public SucursalDto getSucursalById(Long id) {
        Sucursal sucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(SUCURSAL_NO_ENCONTRADA_ID + id));
        return sucursalMapper.toDto(sucursal);
    }


    @Override
    @Transactional(readOnly = true)
    public List<SucursalDto> getAllSucursales() {
        return sucursalRepository.findAll().stream()
                .map(sucursalMapper::toDto)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public SucursalDto getSucursalByCode(String code) {
        Sucursal sucursal = sucursalRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException(SUCURSAL_NO_ENCONTRADA_CODIGO + code));
        return sucursalMapper.toDto(sucursal);
    }


    @Override
    @Transactional(readOnly = true)
    public List<SucursalDto> getSucursalesByLocalidad(String localidad) {
        return sucursalRepository.findByLocalidadContainingIgnoreCase(localidad).stream()
                .map(sucursalMapper::toDto)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public List<SucursalDto> getSucursalesByProvincia(String provincia) {
        return sucursalRepository.findByProvinciaContainingIgnoreCase(provincia).stream()
                .map(sucursalMapper::toDto)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public SucursalDto updateSucursal(Long id, UpdateSucursalDto updateSucursalDto) {
        var sucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(SUCURSAL_NO_ENCONTRADA_ID + id));
        if (updateSucursalDto.getCode() != null && !updateSucursalDto.getCode().equals(sucursal.getCode())) {
            if (sucursalRepository.findByCode(updateSucursalDto.getCode()).isPresent()) {
                throw new IllegalArgumentException(SUCURSAL_DUPLICADA + updateSucursalDto.getCode());
            }
        }
        sucursalMapper.updateEntity(sucursal, updateSucursalDto);
        var updatedSucursal = sucursalRepository.save(sucursal);
        return sucursalMapper.toDto(updatedSucursal);
    }


    @Override
    @Transactional
    public void deleteSucursal(Long id) {
        Sucursal sucursal = sucursalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(SUCURSAL_NO_ENCONTRADA_ID + id));
        if (sucursal.getUsers() != null && !sucursal.getUsers().isEmpty()) {
            throw new IllegalStateException(String.format(SUCURSAL_CON_USUARIOS, sucursal.getUsers().size()));
        }
        sucursalRepository.delete(sucursal);
    }


    @Override
    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        return sucursalRepository.findByCode(code).isPresent();
    }
}
