package com.eljumillano.loop.dtos.order;

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
public class OrderItemSummaryDto {

    private Long id;

    private Long disposableId;

    private String disposableName;

    private Integer quantity;
}
