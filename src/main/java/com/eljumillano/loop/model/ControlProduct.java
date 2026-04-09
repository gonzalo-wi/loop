package com.eljumillano.loop.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "control_products")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ControlProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long    id;

    @ManyToOne
    @JoinColumn(name = "control_id", nullable = false)
    private Control control;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "full_count")
    private int     fullCount;

    @Column(name = "total_count")
    private int     totalCount;

    @Column(name = "replacements")
    private int     replacements;
}
