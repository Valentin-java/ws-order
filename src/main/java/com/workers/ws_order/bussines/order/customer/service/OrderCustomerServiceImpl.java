package com.workers.ws_order.bussines.order.customer.service;

import com.workers.ws_order.bussines.order.customer.interfaces.OrderCustomerService;
import com.workers.ws_order.bussines.order.customer.mapper.OrderMapper;
import com.workers.ws_order.persistance.enums.OrderStatus;
import com.workers.ws_order.persistance.repository.OrderRepository;
import com.workers.ws_order.rest.inbound.dto.createorder.OrderCreateRequestDto;
import com.workers.ws_order.rest.inbound.dto.createorder.OrderCreateResponseDto;
import com.workers.ws_order.rest.inbound.dto.getorder.OrderSummaryDto;
import com.workers.ws_order.rest.inbound.dto.updateorder.OrderUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderCustomerServiceImpl implements OrderCustomerService {

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public OrderCreateResponseDto createOrder(OrderCreateRequestDto requestDto) {
        log.info("Creating a new order for customer ID: {}", requestDto.customerId());

        var orderEntity = orderMapper.toEntity(requestDto);
        orderEntity.setStatus(OrderStatus.NEW);

        orderEntity = orderRepository.save(orderEntity);

        return orderMapper.toResponseDto(orderEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderSummaryDto> getNewOrdersByCustomerId(Long customerId) {
        log.info("Fetching new orders for customer ID: {}", customerId);
        return orderRepository.findByCustomerIdAndStatus(customerId, OrderStatus.NEW)
                .stream()
                .map(orderMapper::toSummaryDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderSummaryDto> getCompletedAndCancelledOrdersByCustomerId(Long customerId) {
        log.info("Fetching completed and cancelled orders for customer ID: {}", customerId);
        return orderRepository.findByCustomerIdAndStatusIn(customerId, List.of(OrderStatus.COMPLETED, OrderStatus.CANCELLED))
                .stream()
                .map(orderMapper::toSummaryDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderCreateResponseDto getOrderDetailsById(Long orderId) {
        log.info("Fetching details for order ID: {}", orderId);
        return orderRepository.findById(orderId)
                .map(orderMapper::toResponseDto)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Order not found with ID: " + orderId));
    }

    @Override
    @Transactional
    public OrderCreateResponseDto updateOrder(Long orderId, OrderUpdateRequestDto requestDto) {
        log.info("Updating order with ID: {}", orderId);

        var orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Order not found with ID: " + orderId));

        orderMapper.updateOrderFromDto(requestDto, orderEntity);

        orderEntity = orderRepository.save(orderEntity);

        return orderMapper.toResponseDto(orderEntity);
    }
}
