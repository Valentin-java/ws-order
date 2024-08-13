package com.workers.ws_order.bussines.order.service;

import com.workers.ws_order.bussines.order.interfaces.OrderService;
import com.workers.ws_order.bussines.order.mapper.OrderMapper;
import com.workers.ws_order.persistance.entity.OrderEntity;
import com.workers.ws_order.persistance.entity.OrderPhotoEntity;
import com.workers.ws_order.persistance.enums.BidStatus;
import com.workers.ws_order.persistance.enums.OrderStatus;
import com.workers.ws_order.persistance.repository.BidRepository;
import com.workers.ws_order.persistance.repository.OrderPhotoRepository;
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

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final BidRepository bidRepository;
    private final OrderRepository orderRepository;
    private final OrderPhotoRepository orderPhotoRepository;

    @Override
    @Transactional
    public OrderCreateResponseDto createOrder(OrderCreateRequestDto requestDto) {
        log.info("Creating a new order for customer ID: {}", requestDto.customerId());

        // Шаг 1: Создание сущности заказа
        OrderEntity orderEntity = createOrderEntity(requestDto);

        // Шаг 2: Сохранение заказа в базе данных
        orderEntity = orderRepository.save(orderEntity);

        // Шаг 3: Сохранение фотографий заказа
        List<OrderPhotoEntity> photos = saveOrderPhotos(requestDto.photoData(), orderEntity);

        // Шаг 4: Формирование и возврат ответа
        OrderCreateResponseDto responseDto = mapToResponseDto(orderEntity, photos);

        log.info("Order created successfully with ID: {}", responseDto.orderId());
        return responseDto;
    }

    private OrderEntity createOrderEntity(OrderCreateRequestDto requestDto) {
        OrderEntity orderEntity = orderMapper.toEntity(requestDto);
        orderEntity.setStatus(OrderStatus.NEW);
        return orderEntity;
    }

    private List<OrderPhotoEntity> saveOrderPhotos(List<byte[]> photoDataList, OrderEntity orderEntity) {
        List<OrderPhotoEntity> photos = new ArrayList<>();
        for (byte[] photoData : photoDataList) {
            OrderPhotoEntity photoEntity = new OrderPhotoEntity();
            photoEntity.setPhotoData(photoData);
            photoEntity.setOrder(orderEntity);
            photos.add(photoEntity);
        }
        return orderPhotoRepository.saveAll(photos);
    }

    private OrderCreateResponseDto mapToResponseDto(OrderEntity orderEntity, List<OrderPhotoEntity> photos) {
        orderEntity.setPhotos(photos);
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

        OrderEntity orderEntity = findOrderById(orderId);
        updateOrderFields(orderEntity, requestDto);
        updateOrderPhotos(orderEntity, requestDto.photoData());

        orderEntity = saveOrder(orderEntity);
        return mapToResponseDto(orderEntity);
    }

    private OrderEntity findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Order not found with ID: " + orderId));
    }

    private void updateOrderFields(OrderEntity orderEntity, OrderUpdateRequestDto requestDto) {
        orderMapper.updateOrderFromDto(requestDto, orderEntity);
    }

    private void updateOrderPhotos(OrderEntity orderEntity, List<byte[]> photoData) {
        if (photoData != null && !photoData.isEmpty()) {
            orderPhotoRepository.deleteAll(orderEntity.getPhotos());
            List<OrderPhotoEntity> photos = mapPhotoDataToEntities(photoData, orderEntity);
            orderPhotoRepository.saveAll(photos);
            orderEntity.setPhotos(photos);
        }
    }

    private List<OrderPhotoEntity> mapPhotoDataToEntities(List<byte[]> photoData, OrderEntity orderEntity) {
        return photoData.stream()
                .map(data -> {
                    OrderPhotoEntity photoEntity = new OrderPhotoEntity();
                    photoEntity.setPhotoData(data);
                    photoEntity.setOrder(orderEntity);
                    return photoEntity;
                })
                .toList();
    }

    private OrderEntity saveOrder(OrderEntity orderEntity) {
        return orderRepository.save(orderEntity);
    }

    private OrderCreateResponseDto mapToResponseDto(OrderEntity orderEntity) {
        return orderMapper.toResponseDto(orderEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderSummaryDto> getAvailableOrdersForSpecialist(Long specialistId) {
        log.info("Fetching available orders for specialist ID: {}", specialistId);

        return orderRepository.findAllByStatus(OrderStatus.NEW)
                .stream()
                .filter(order -> !bidRepository.existsByOrderIdAndStatus(order.getId(), BidStatus.ACCEPTED))
                .map(orderMapper::toSummaryDto)
                .toList();
    }

}
