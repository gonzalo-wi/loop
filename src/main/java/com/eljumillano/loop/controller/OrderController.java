package com.eljumillano.loop.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eljumillano.loop.dtos.order.CreateOrderDto;
import com.eljumillano.loop.dtos.order.OrderDto;
import com.eljumillano.loop.dtos.order.UpdateOrderStateDto;
import com.eljumillano.loop.model.enums.OrderState;
import com.eljumillano.loop.service.iservice.IOrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/loop/api/orders")
public class OrderController {

    private final IOrderService orderService;

    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }


    @GetMapping
    public ResponseEntity<List<OrderDto>> getAll() {
        return ResponseEntity.ok(orderService.getAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getById(id));
    }


    @PostMapping
    public ResponseEntity<OrderDto> create(@Valid @RequestBody CreateOrderDto orderDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(orderDto));
    }

    
    @PutMapping("/{id}")
    public ResponseEntity<OrderDto> update(@PathVariable Long id, @Valid @RequestBody CreateOrderDto orderDto) {
        return ResponseEntity.ok(orderService.update(id, orderDto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
    

    @PatchMapping("/{id}/state")
    public ResponseEntity<OrderDto> updateState(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStateDto stateDto) {
        return ResponseEntity.ok(orderService.updateState(id, stateDto.getState()));
    }


    @GetMapping("/today")
    public ResponseEntity<List<OrderDto>> getTodayOrders(
            @RequestParam(required = false) OrderState state) {
        if (state != null) {
            return ResponseEntity.ok(orderService.getTodayOrdersByState(state));
        }
        return ResponseEntity.ok(orderService.getTodayOrders());
    }
}
