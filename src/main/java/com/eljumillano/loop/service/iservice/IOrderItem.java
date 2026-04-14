package com.eljumillano.loop.service.iservice;
import java.util.List;
import com.eljumillano.loop.dtos.orderItem.CreateOrderItemDto;
import com.eljumillano.loop.dtos.orderItem.OrderItemDto;

public interface IOrderItem {
    public List<OrderItemDto> getByOrderId(Long orderId);
    public OrderItemDto getById(Long id);
    public OrderItemDto create(Long orderId, CreateOrderItemDto orderItemDto);
    public OrderItemDto update(Long id, CreateOrderItemDto orderItemDto);
    public void delete(Long id);
}
