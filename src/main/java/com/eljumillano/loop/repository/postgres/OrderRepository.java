package com.eljumillano.loop.repository.postgres;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.eljumillano.loop.model.Order;
import com.eljumillano.loop.model.enums.OrderState;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
    @Query("SELECT o FROM Order o WHERE DATE(o.orderDate) = DATE(:date)")
    List<Order> findByOrderDate(@Param("date") LocalDateTime date);
    
    @Query("SELECT o FROM Order o WHERE DATE(o.orderDate) = DATE(:date) AND o.state = :state")
    List<Order> findByOrderDateAndState(@Param("date") LocalDateTime date, @Param("state") OrderState state);
    
    @Query("SELECT o FROM Order o WHERE DATE(o.orderDate) = DATE(:date) AND o.delivery.id = :deliveryId")
    List<Order> findByOrderDateAndDeliveryId(@Param("date") LocalDateTime date, @Param("deliveryId") Long deliveryId);
    
    List<Order> findByState(OrderState state);
}
