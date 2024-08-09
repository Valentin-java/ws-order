package com.workers.ws_order.bussines.createorder.interfaces;

import com.workers.ws_order.rest.inbound.dto.createorder.OrderCreateRequestDto;
import com.workers.ws_order.rest.inbound.dto.createorder.OrderCreateResponseDto;
import com.workers.ws_order.rest.inbound.dto.getorder.OrderSummaryDto;

import java.util.List;

public interface OrderService {

    OrderCreateResponseDto createOrder(OrderCreateRequestDto requestDto);

    List<OrderSummaryDto> getNewOrdersByCustomerId(Long customerId);

    List<OrderSummaryDto> getCompletedAndCancelledOrdersByCustomerId(Long customerId);
}
