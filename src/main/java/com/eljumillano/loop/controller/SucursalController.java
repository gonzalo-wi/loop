package com.eljumillano.loop.controller;

import com.eljumillano.loop.dtos.sucursal.CreateSucursalDto;
import com.eljumillano.loop.dtos.sucursal.SucursalDto;
import com.eljumillano.loop.dtos.sucursal.UpdateSucursalDto;
import com.eljumillano.loop.service.iservice.ISucursalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loop/api/sucursal")
@CrossOrigin(origins = "*")
public class SucursalController {

    @Autowired
    private ISucursalService sucursalService;

    /**
     * Crear una nueva sucursal
     * POST /loop/api/sucursal/register
     */
    @PostMapping("/register")
    public ResponseEntity<SucursalDto> createSucursal(@Valid @RequestBody CreateSucursalDto createSucursalDto) {
        SucursalDto sucursalDto = sucursalService.createSucursal(createSucursalDto);
        return new ResponseEntity<>(sucursalDto, HttpStatus.CREATED);
    }

    /**
     * Obtener todas las sucursales
     * GET /loop/api/sucursales
     */
    @GetMapping
    public ResponseEntity<List<SucursalDto>> getAllSucursales() {
        List<SucursalDto> sucursales = sucursalService.getAllSucursales();
        return ResponseEntity.ok(sucursales);
    }

    /**
     * Obtener sucursal por ID
     * GET /loop/api/sucursales/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<SucursalDto> getSucursalById(@PathVariable Long id) {
        SucursalDto sucursalDto = sucursalService.getSucursalById(id);
        return ResponseEntity.ok(sucursalDto);
    }

    /**
     * Obtener sucursal por código
     * GET /loop/api/sucursales/code/{code}
     */
    @GetMapping("/code/{code}")
    public ResponseEntity<SucursalDto> getSucursalByCode(@PathVariable String code) {
        SucursalDto sucursalDto = sucursalService.getSucursalByCode(code);
        return ResponseEntity.ok(sucursalDto);
    }

    /**
     * Obtener sucursales por localidad
     * GET /loop/api/sucursales/localidad/{localidad}
     */
    @GetMapping("/localidad/{localidad}")
    public ResponseEntity<List<SucursalDto>> getSucursalesByLocalidad(@PathVariable String localidad) {
        List<SucursalDto> sucursales = sucursalService.getSucursalesByLocalidad(localidad);
        return ResponseEntity.ok(sucursales);
    }

    /**
     * Obtener sucursales por provincia
     * GET /loop/api/sucursales/provincia/{provincia}
     */
    @GetMapping("/provincia/{provincia}")
    public ResponseEntity<List<SucursalDto>> getSucursalesByProvincia(@PathVariable String provincia) {
        List<SucursalDto> sucursales = sucursalService.getSucursalesByProvincia(provincia);
        return ResponseEntity.ok(sucursales);
    }

    /**
     * Actualizar sucursal
     * PUT /loop/api/sucursales/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<SucursalDto> updateSucursal(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSucursalDto updateSucursalDto) {
        SucursalDto sucursalDto = sucursalService.updateSucursal(id, updateSucursalDto);
        return ResponseEntity.ok(sucursalDto);
    }

    /**
     * Eliminar sucursal
     * DELETE /loop/api/sucursales/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSucursal(@PathVariable Long id) {
        sucursalService.deleteSucursal(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Verificar si existe una sucursal por código
     * GET /loop/api/sucursales/exists/{code}
     */
    @GetMapping("/exists/{code}")
    public ResponseEntity<Boolean> existsByCode(@PathVariable String code) {
        boolean exists = sucursalService.existsByCode(code);
        return ResponseEntity.ok(exists);
    }
}
