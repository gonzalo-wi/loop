package com.eljumillano.loop.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eljumillano.loop.dtos.orderItem.CreateOrderItemDto;
import com.eljumillano.loop.dtos.orderItem.OrderItemDto;
import com.eljumillano.loop.service.iservice.IOrderItem;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/loop/api/orders/{orderId}/items")
public class OrderItemController {

    private final IOrderItem orderItemService;

    public OrderItemController(IOrderItem orderItemService) {
        this.orderItemService = orderItemService;
    }

    
    @GetMapping
    public ResponseEntity<List<OrderItemDto>> getByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderItemService.getByOrderId(orderId));
    }


    @GetMapping("/{id}")
    public ResponseEntity<OrderItemDto> getById(@PathVariable Long orderId, @PathVariable Long id) {
        return ResponseEntity.ok(orderItemService.getById(id));
    }


    @PostMapping
    public ResponseEntity<OrderItemDto> create(
            @PathVariable Long orderId,
            @Valid @RequestBody CreateOrderItemDto orderItemDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderItemService.create(orderId, orderItemDto));
    }


    @PutMapping("/{id}")
    public ResponseEntity<OrderItemDto> update(
            @PathVariable Long orderId,
            @PathVariable Long id,
            @Valid @RequestBody CreateOrderItemDto orderItemDto) {
        return ResponseEntity.ok(orderItemService.update(id, orderItemDto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long orderId, @PathVariable Long id) {
        orderItemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
