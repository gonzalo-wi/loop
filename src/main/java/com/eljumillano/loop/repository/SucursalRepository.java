package com.eljumillano.loop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eljumillano.loop.model.Sucursal;

public interface SucursalRepository extends JpaRepository<Sucursal, Long> {
    
}
