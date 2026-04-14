package com.eljumillano.loop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eljumillano.loop.model.PackingSlip;

@Repository
public interface PackingSlipRepository extends JpaRepository<PackingSlip, Long> {
    
    Optional<PackingSlip> findBySlipNumber(String slipNumber);
    
    List<PackingSlip> findByDeliveryId(Long deliveryId);
    
    List<PackingSlip> findByControlId(Long controlId);
    
    List<PackingSlip> findByOrderId(Long orderId);
    
    @Query("SELECT ps FROM PackingSlip ps WHERE ps.sucursalId = :sucursalId ORDER BY ps.slipNumber DESC")
    List<PackingSlip> findBySucursalIdOrderBySlipNumberDesc(@Param("sucursalId") Long sucursalId);
    
    @Query("SELECT MAX(CAST(SUBSTRING(ps.slipNumber, LOCATE('-', ps.slipNumber) + 1, LENGTH(ps.slipNumber)) AS long)) " +
           "FROM PackingSlip ps WHERE ps.sucursalId = :sucursalId")
    Long findMaxConsecutivoBySucursal(@Param("sucursalId") Long sucursalId);
}
