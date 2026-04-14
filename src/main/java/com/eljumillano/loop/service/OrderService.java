package com.eljumillano.loop.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eljumillano.loop.dtos.order.CreateOrderDto;
import com.eljumillano.loop.dtos.order.OrderDto;
import com.eljumillano.loop.dtos.orderItem.CreateOrderItemDto;
import com.eljumillano.loop.exception.ResourceNotFoundException;
import com.eljumillano.loop.mapper.OrderItemMapper;
import com.eljumillano.loop.mapper.OrderMapper;
import com.eljumillano.loop.model.Disposable;
import com.eljumillano.loop.model.Order;
import com.eljumillano.loop.model.OrderItem;
import com.eljumillano.loop.model.Sucursal;
import com.eljumillano.loop.model.User;
import com.eljumillano.loop.model.enums.OrderState;
import com.eljumillano.loop.repository.DisposableRepository;
import com.eljumillano.loop.repository.OrderRepository;
import com.eljumillano.loop.repository.SucursalRepository;
import com.eljumillano.loop.repository.UserRepository;
import com.eljumillano.loop.service.iservice.IOrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService implements IOrderService {

    private static final String        MESSAGE_ENTITY_NAME            = "Order";
    private static final String        MESSAGE_USER_ENTITY_NAME       = "User";
    private static final String        MESSAGE_SUCURSAL_ENTITY_NAME   = "Sucursal";
    private static final String        MESSAGE_DISPOSABLE_ENTITY_NAME = "Disposable";
    private final OrderRepository      orderRepository;
    private final OrderMapper          orderMapper;
    private final UserRepository       userRepository;
    private final SucursalRepository   sucursalRepository;
    private final OrderItemMapper      orderItemMapper;
    private final DisposableRepository disposableRepository;


    @Override
    public List<OrderDto> getAll() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }


    @Override
    public OrderDto getById(Long id) {
        return orderRepository.findById(id)
                .map(orderMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(MESSAGE_ENTITY_NAME, id));
    }


    @Override
    @Transactional
    public OrderDto create(CreateOrderDto orderDto) {
        User delivery = validateAndGetDelivery(orderDto.getDeliveryId());
        var sucursal  = validateAndGetSucursal(orderDto.getSucursalId());
        var order     = orderMapper.toEntity(orderDto);
        order.setDelivery(delivery);
        order.setSucursal(sucursal);
        addItemsToOrder(order, orderDto.getItems());
        return orderMapper.toDto(orderRepository.save(order));
    }


    @Override
    @Transactional
    public OrderDto update(Long id, CreateOrderDto orderDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MESSAGE_ENTITY_NAME, id));
        User delivery = validateAndGetDelivery(orderDto.getDeliveryId());
        Sucursal sucursal = validateAndGetSucursal(orderDto.getSucursalId());
        orderMapper.updateEntity(orderDto, order);
        order.setDelivery(delivery);
        order.setSucursal(sucursal);
        order.getItems().clear();
        addItemsToOrder(order, orderDto.getItems());
        
        return orderMapper.toDto(orderRepository.save(order));
    }


    @Override
    @Transactional
    public void delete(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException(MESSAGE_ENTITY_NAME, id);
        }
        orderRepository.deleteById(id);
    }


    @Override
    @Transactional
    public OrderDto updateState(Long id, OrderState state) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MESSAGE_ENTITY_NAME, id));
        
        order.setState(state);
        return orderMapper.toDto(orderRepository.save(order));
    }


    @Override
    public List<OrderDto> getTodayOrders() {
        return orderRepository.findByOrderDate(LocalDateTime.now())
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }


    
    private User validateAndGetDelivery(Long deliveryId) {
        return userRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException(MESSAGE_USER_ENTITY_NAME, deliveryId));
    }

    private Sucursal validateAndGetSucursal(Long sucursalId) {
        return sucursalRepository.findById(sucursalId)
                .orElseThrow(() -> new ResourceNotFoundException(MESSAGE_SUCURSAL_ENTITY_NAME, sucursalId));
    }


    private Disposable validateAndGetDisposable(Long disposableId) {
        return disposableRepository.findById(disposableId)
                .orElseThrow(() -> new ResourceNotFoundException(MESSAGE_DISPOSABLE_ENTITY_NAME, disposableId));
    }

  
    private void addItemsToOrder(Order order, List<CreateOrderItemDto> itemDtos) {
        if (itemDtos == null || itemDtos.isEmpty()) {
            return;
        }
        itemDtos.forEach(itemDto -> {
            Disposable disposable = validateAndGetDisposable(itemDto.getDisposableId());
            OrderItem orderItem   = orderItemMapper.toEntity(itemDto);
            orderItem.setOrder(order);
            orderItem.setDisposable(disposable);
            order.getItems().add(orderItem);
        });
    }


    @Override
    public List<OrderDto> getTodayOrdersByState(OrderState state) {
        return orderRepository.findByOrderDateAndState(LocalDateTime.now(), state)
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }
}
