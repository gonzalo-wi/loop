package com.eljumillano.loop.repository.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import com.eljumillano.loop.model.Sucursal;

import java.util.List;
import java.util.Optional;

public interface SucursalRepository extends JpaRepository<Sucursal, Long> {
    
    Optional<Sucursal> findByCode(String code);
    
    List<Sucursal> findByLocalidadContainingIgnoreCase(String localidad);
    
    List<Sucursal> findByProvinciaContainingIgnoreCase(String provincia);
}
