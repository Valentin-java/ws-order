package com.workers.ws_order.bussines.order.service;

import com.workers.ws_order.rest.inbound.dto.common.model.pagination.PageDomain;
import com.workers.ws_order.bussines.order.interfaces.OrderService;
import com.workers.ws_order.bussines.order.mapper.OrderMapper;
import com.workers.ws_order.persistance.entity.BidEntity;
import com.workers.ws_order.persistance.entity.OrderEntity;
import com.workers.ws_order.persistance.enums.BidStatus;
import com.workers.ws_order.persistance.enums.OrderStatus;
import com.workers.ws_order.persistance.projections.OrderSummaryProjection;
import com.workers.ws_order.persistance.repository.BidRepository;
import com.workers.ws_order.persistance.repository.OrderRepository;
import com.workers.ws_order.persistance.repository.custom.OrderPageableCustomRepository;
import com.workers.ws_order.rest.inbound.dto.createorder.OrderCreateRequestDto;
import com.workers.ws_order.rest.inbound.dto.createorder.OrderCreateResponseDto;
import com.workers.ws_order.rest.inbound.dto.getorder.OrderSummaryDto;
import com.workers.ws_order.rest.inbound.dto.getorder.OrderSummaryRequestDto;
import com.workers.ws_order.rest.inbound.dto.updateorder.OrderUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final BidRepository bidRepository;
    private final OrderRepository orderRepository;
    private final OrderPageableCustomRepository orderPageableCustomRepository;

    @Override
    @Transactional
    public OrderCreateResponseDto createOrder(OrderCreateRequestDto requestDto) {
        log.info("Creating a new order for customer ID: {}", requestDto.customerId());

        var orderEntity = orderRepository.save(createOrderEntity(requestDto));
        return orderMapper.toResponseDto(orderEntity);
    }

    private OrderEntity createOrderEntity(OrderCreateRequestDto requestDto) {
        OrderEntity orderEntity = orderMapper.toEntity(requestDto);
        orderEntity.setStatus(OrderStatus.NEW);
        return orderEntity;
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

        OrderEntity orderEntity = findOrderById(orderId);
        updateOrderFields(orderEntity, requestDto);

        orderEntity = saveOrder(orderEntity);
        return orderMapper.toResponseDto(orderEntity);
    }

    private OrderEntity findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Order not found with ID: " + orderId));
    }

    private void updateOrderFields(OrderEntity orderEntity, OrderUpdateRequestDto requestDto) {
        orderMapper.updateOrderFromDto(requestDto, orderEntity);
    }

    private OrderEntity saveOrder(OrderEntity orderEntity) {
        return orderRepository.save(orderEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public PageDomain<OrderSummaryProjection> getAvailableOrdersForSpecialist(OrderSummaryRequestDto requestDto)
            throws ExecutionException, InterruptedException {
        log.info("Fetching available orders for specialist ID: {}", requestDto);


        var futureRecordCount = CompletableFuture.supplyAsync(
                () -> orderPageableCustomRepository.getRecordsCount(requestDto.filter()));
        var orderList = orderPageableCustomRepository.getOrderListByFilter(requestDto);

        return new PageDomain<>(
                requestDto.pageable().getOffset(),
                requestDto.pageable().getItemsLimit(),
                futureRecordCount.get(),
                orderList);
    }

    @Override
    @Transactional
    public void completeOrder(Long orderId, Long specialistId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Order not found with ID: " + orderId));

        BidEntity acceptedBid = bidRepository.findFirstByOrderIdAndStatus(orderId, BidStatus.ACCEPTED);
        if (acceptedBid == null || !acceptedBid.getSpecialistId().equals(specialistId)) {
            throw new ResponseStatusException(BAD_REQUEST, "Only the specialist with the accepted bid can complete the order");
        }

        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);
        log.info("Order with ID: {} has been marked as completed by specialist ID: {}", orderId, specialistId);
    }

}
