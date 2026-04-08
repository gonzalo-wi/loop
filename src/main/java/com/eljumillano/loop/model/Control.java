package com.eljumillano.loop.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "controls")
@Getter @Setter 
@NoArgsConstructor
@AllArgsConstructor
public class Control {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;    
    
    @Column(name = "delivery_id")
    private Long deliveryId;
    
    @Column(name = "supervisor_id")
    private Long supervisorId;
    
    @OneToMany(mappedBy = "control")
    private List<Product> supervisorProducts;
    
    @Column(name = "sucursal_id")
    private Long sucursalId;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
