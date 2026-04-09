package com.eljumillano.loop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eljumillano.loop.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
  
}
