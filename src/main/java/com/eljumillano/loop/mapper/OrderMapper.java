package com.eljumillano.loop.mapper;

import java.math.BigDecimal;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.eljumillano.loop.dtos.order.CreateOrderDto;
import com.eljumillano.loop.dtos.order.OrderDto;
import com.eljumillano.loop.dtos.order.OrderItemSummaryDto;
import com.eljumillano.loop.model.Order;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final OrderItemMapper orderItemMapper;

    public OrderDto toDto(Order order) {
        if (order == null) {
            return null;
        }

        return OrderDto.builder()
                .id(order.getId())
                .deliveryId(order.getDelivery().getId())
                .deliveryName(order.getDelivery().getName() + " " + order.getDelivery().getLastName())
                .sucursalId(order.getSucursal().getId())
                .sucursalName(order.getSucursal().getName())
                .orderDate(order.getOrderDate())
                .total(order.getTotal() != null ? order.getTotal().intValue() : null)
                .state(order.getState())
                .items(order.getItems().stream()
                        .map(orderItemMapper::toSummaryDto)
                        .collect(Collectors.toList()))
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    public Order toEntity(CreateOrderDto dto) {
        if (dto == null) {
            return null;
        }

        Order order = new Order();
        order.setOrderDate(dto.getOrderDate());
        order.setTotal(dto.getTotal() != null ? BigDecimal.valueOf(dto.getTotal()) : null);
        return order;
    }

    public void updateEntity(CreateOrderDto dto, Order order) {
        if (dto == null || order == null) {
            return;
        }

        order.setOrderDate(dto.getOrderDate());
        order.setTotal(dto.getTotal() != null ? BigDecimal.valueOf(dto.getTotal()) : null);
    }
}
