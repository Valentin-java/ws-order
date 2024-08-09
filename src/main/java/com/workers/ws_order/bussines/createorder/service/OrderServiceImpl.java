package com.workers.ws_order.bussines.createorder.service;

import com.workers.ws_order.bussines.createorder.interfaces.OrderService;
import com.workers.ws_order.bussines.createorder.mapper.OrderMapper;
import com.workers.ws_order.persistance.entity.OrderEntity;
import com.workers.ws_order.persistance.entity.OrderPhotoEntity;
import com.workers.ws_order.persistance.enums.OrderStatus;
import com.workers.ws_order.persistance.repository.OrderPhotoRepository;
import com.workers.ws_order.persistance.repository.OrderRepository;
import com.workers.ws_order.rest.inbound.dto.createorder.OrderCreateRequestDto;
import com.workers.ws_order.rest.inbound.dto.createorder.OrderCreateResponseDto;
import com.workers.ws_order.rest.inbound.dto.getorder.OrderSummaryDto;
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

    private final OrderRepository orderRepository;
    private final OrderPhotoRepository orderPhotoRepository;
    private final OrderMapper orderMapper;

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

    // Вспомогательный метод для создания сущности заказа
    private OrderEntity createOrderEntity(OrderCreateRequestDto requestDto) {
        OrderEntity orderEntity = orderMapper.toEntity(requestDto);
        orderEntity.setStatus(OrderStatus.NEW);
        return orderEntity;
    }

    // Вспомогательный метод для сохранения фотографий заказа
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

    // Вспомогательный метод для маппинга ответа
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

}
