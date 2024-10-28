package com.workers.ws_order.bussines.order.interfaces;

import com.workers.ws_order.rest.inbound.dto.common.model.pagination.PageDomain;
import com.workers.ws_order.persistance.projections.OrderSummaryProjection;
import com.workers.ws_order.rest.inbound.dto.createorder.OrderCreateRequestDto;
import com.workers.ws_order.rest.inbound.dto.createorder.OrderCreateResponseDto;
import com.workers.ws_order.rest.inbound.dto.getorder.OrderSummaryDto;
import com.workers.ws_order.rest.inbound.dto.getorder.OrderSummaryRequestDto;
import com.workers.ws_order.rest.inbound.dto.updateorder.OrderUpdateRequestDto;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface OrderService {

    OrderCreateResponseDto createOrder(OrderCreateRequestDto requestDto);

    List<OrderSummaryDto> getNewOrdersByCustomerId(Long customerId);

    List<OrderSummaryDto> getCompletedAndCancelledOrdersByCustomerId(Long customerId);

    OrderCreateResponseDto getOrderDetailsById(Long orderId);

    OrderCreateResponseDto updateOrder(Long orderId, OrderUpdateRequestDto requestDto);

    PageDomain<OrderSummaryProjection> getAvailableOrdersForSpecialist(OrderSummaryRequestDto requestDto) throws ExecutionException, InterruptedException;

    void completeOrder(Long orderId, Long specialistId);
}
