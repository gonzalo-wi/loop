package com.eljumillano.loop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eljumillano.loop.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderId(Long orderId);
}
