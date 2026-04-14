package com.eljumillano.loop.service.iservice;
import java.util.List;
import com.eljumillano.loop.dtos.order.CreateOrderDto;
import com.eljumillano.loop.dtos.order.OrderDto;
import com.eljumillano.loop.model.enums.OrderState;

public interface IOrderService {
    public List<OrderDto> getAll();
    public OrderDto getById(Long id);   
    public OrderDto create(CreateOrderDto orderDto);
    public OrderDto update(Long id, CreateOrderDto orderDto);
    public void delete(Long id);
    public OrderDto updateState(Long id, OrderState state);
    public List<OrderDto> getTodayOrders();
    public List<OrderDto> getTodayOrdersByState(OrderState state);
}
