package com.eljumillano.loop.dtos.order;

import java.time.LocalDateTime;
import java.util.List;

import com.eljumillano.loop.model.enums.OrderState;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {

    private Long id;

    private Long deliveryId;

    private String deliveryName;

    private Long sucursalId;

    private String sucursalName;

    private LocalDateTime orderDate;

    private Integer total;

    private OrderState state;

    private List<OrderItemSummaryDto> items;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
