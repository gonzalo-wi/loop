package com.eljumillano.loop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eljumillano.loop.dtos.orderItem.CreateOrderItemDto;
import com.eljumillano.loop.dtos.orderItem.OrderItemDto;
import com.eljumillano.loop.exception.ResourceNotFoundException;
import com.eljumillano.loop.mapper.OrderItemMapper;
import com.eljumillano.loop.model.Disposable;
import com.eljumillano.loop.model.Order;
import com.eljumillano.loop.model.OrderItem;
import com.eljumillano.loop.repository.DisposableRepository;
import com.eljumillano.loop.repository.OrderItemRepository;
import com.eljumillano.loop.repository.OrderRepository;
import com.eljumillano.loop.service.iservice.IOrderItem;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderItemService implements IOrderItem {

    private static final String MESSAGE_ENTITY_NAME            = "OrderItem";
    private static final String MESSAGE_ORDER_ENTITY_NAME      = "Order";
    private static final String MESSAGE_DISPOSABLE_ENTITY_NAME = "Disposable";

    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final DisposableRepository disposableRepository;
    private final OrderRepository orderRepository;

    
    @Override
    public List<OrderItemDto> getByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId)
                .stream()
                .map(orderItemMapper::toDto)
                .toList();
    }


    @Override
    public OrderItemDto getById(Long id) {
        return orderItemRepository.findById(id)
                .map(orderItemMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(MESSAGE_ENTITY_NAME, id));
    }


    @Override
    @Transactional
    public OrderItemDto create(Long orderId, CreateOrderItemDto orderItemDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(MESSAGE_ORDER_ENTITY_NAME, orderId));
        
        Disposable disposable = disposableRepository.findById(orderItemDto.getDisposableId())
                .orElseThrow(() -> new ResourceNotFoundException(MESSAGE_DISPOSABLE_ENTITY_NAME, orderItemDto.getDisposableId()));
        
        OrderItem orderItem = orderItemMapper.toEntity(orderItemDto);
        orderItem.setOrder(order);
        orderItem.setDisposable(disposable);
        
        return orderItemMapper.toDto(orderItemRepository.save(orderItem));
    }


    @Override
    @Transactional
    public OrderItemDto update(Long id, CreateOrderItemDto orderItemDto) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MESSAGE_ENTITY_NAME, id));
        
        Disposable disposable = disposableRepository.findById(orderItemDto.getDisposableId())
                .orElseThrow(() -> new ResourceNotFoundException(MESSAGE_DISPOSABLE_ENTITY_NAME, orderItemDto.getDisposableId()));
        
        orderItemMapper.updateEntity(orderItemDto, orderItem);
        orderItem.setDisposable(disposable);
        
        return orderItemMapper.toDto(orderItemRepository.save(orderItem));
    }



    @Override
    @Transactional
    public void delete(Long id) {
        if (!orderItemRepository.existsById(id)) {
            throw new ResourceNotFoundException(MESSAGE_ENTITY_NAME, id);
        }
        orderItemRepository.deleteById(id);
    }
    
}
