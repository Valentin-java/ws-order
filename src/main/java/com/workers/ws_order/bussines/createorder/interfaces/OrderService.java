package com.workers.ws_order.bussines.createorder.interfaces;

import com.workers.ws_order.rest.inbound.dto.createorder.OrderCreateRequestDto;
import com.workers.ws_order.rest.inbound.dto.createorder.OrderCreateResponseDto;

public interface OrderService {
    OrderCreateResponseDto createOrder(OrderCreateRequestDto requestDto);
}
