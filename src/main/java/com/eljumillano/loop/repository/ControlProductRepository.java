package com.eljumillano.loop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.eljumillano.loop.model.ControlProduct;

@Repository
public interface ControlProductRepository extends JpaRepository<ControlProduct, Long> {
}
