package com.eljumillano.loop.model;

import java.time.LocalDateTime;
import java.util.List;

import com.eljumillano.loop.model.enums.TypeControl;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    private Long                 id;    
    
    @Column(name = "delivery_id")
    private Long                 deliveryId;
    
    @Column(name = "supervisor_id")
    private Long                 supervisorId;
    
    @OneToMany(mappedBy = "control", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ControlProduct> products;
    
    @Column(name = "sucursal_id")
    private Long                 sucursalId;

    @Column(name = "checked", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean checked;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_control", nullable = false)
    private TypeControl          typeControl;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime        createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
