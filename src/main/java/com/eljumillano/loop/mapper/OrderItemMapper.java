package com.eljumillano.loop.mapper;

import org.springframework.stereotype.Component;

import com.eljumillano.loop.dtos.order.OrderItemSummaryDto;
import com.eljumillano.loop.dtos.orderItem.CreateOrderItemDto;
import com.eljumillano.loop.dtos.orderItem.OrderItemDto;
import com.eljumillano.loop.model.OrderItem;

@Component
public class OrderItemMapper {

    public OrderItemDto toDto(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }

        return OrderItemDto.builder()
                .id(orderItem.getId())
                .orderId(orderItem.getOrder().getId())
                .disposableId(orderItem.getDisposable().getId())
                .disposableName(orderItem.getDisposable().getName())
                .quantity(orderItem.getQuantity())
                .createdAt(orderItem.getCreatedAt())
                .updatedAt(orderItem.getUpdatedAt())
                .build();
    }

    public OrderItemSummaryDto toSummaryDto(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }

        return OrderItemSummaryDto.builder()
                .id(orderItem.getId())
                .disposableId(orderItem.getDisposable().getId())
                .disposableName(orderItem.getDisposable().getName())
                .quantity(orderItem.getQuantity())
                .build();
    }

    public OrderItem toEntity(CreateOrderItemDto dto) {
        if (dto == null) {
            return null;
        }

        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(dto.getQuantity());
        return orderItem;
    }

    public void updateEntity(CreateOrderItemDto dto, OrderItem orderItem) {
        if (dto == null || orderItem == null) {
            return;
        }

        orderItem.setQuantity(dto.getQuantity());
    }
}
