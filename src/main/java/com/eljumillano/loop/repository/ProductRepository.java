package com.eljumillano.loop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eljumillano.loop.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    
}
