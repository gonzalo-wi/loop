package com.eljumillano.loop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eljumillano.loop.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
}
