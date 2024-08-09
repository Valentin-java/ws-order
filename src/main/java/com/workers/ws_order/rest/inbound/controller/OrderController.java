package com.workers.ws_order.rest.inbound.controller;

import com.workers.ws_order.bussines.createorder.interfaces.OrderService;
import com.workers.ws_order.rest.inbound.dto.createorder.OrderCreateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody OrderCreateRequestDto requestDto) {
        return ResponseEntity.ok(orderService.createOrder(requestDto));
    }

}
