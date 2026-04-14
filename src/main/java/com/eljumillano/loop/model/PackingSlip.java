package com.eljumillano.loop.model;

import java.time.LocalDateTime;

import com.eljumillano.loop.model.enums.PackingSlipStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "packing_slips")
@Getter @Setter 
@NoArgsConstructor
@AllArgsConstructor
public class PackingSlip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "slip_number", nullable = false, unique = true, length = 50)
    private String slipNumber;  // Formato: 00202-00345476
    
    @ManyToOne
    @JoinColumn(name = "control_id", nullable = false)
    private Control control;
    
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = true)  // La orden es opcional
    private Order order;
    
    @Column(name = "delivery_id", nullable = false)
    private Long deliveryId;
    
    @Column(name = "supervisor_id", nullable = false)
    private Long supervisorId;  // Quien controló
    
    @Column(name = "sucursal_id", nullable = false)
    private Long sucursalId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PackingSlipStatus status;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "observations", columnDefinition = "TEXT")
    private String observations;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = PackingSlipStatus.ACTIVE;
        }
    }
}
