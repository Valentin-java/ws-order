package com.workers.ws_order.rest.inbound.controller;

import com.workers.ws_order.bussines.createorder.interfaces.OrderService;
import com.workers.ws_order.rest.inbound.dto.createorder.OrderCreateRequestDto;
import com.workers.ws_order.rest.inbound.dto.createorder.OrderCreateResponseDto;
import com.workers.ws_order.rest.inbound.dto.getorder.OrderSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody OrderCreateRequestDto requestDto) {
        return ResponseEntity.ok(orderService.createOrder(requestDto));
    }

    @GetMapping("/active")
    public ResponseEntity<List<OrderSummaryDto>> getNewOrdersByCustomerId(@RequestParam Long customerId) {
        return ResponseEntity.ok(orderService.getNewOrdersByCustomerId(customerId));
    }

    @GetMapping("/archive")
    public ResponseEntity<List<OrderSummaryDto>> getCompletedAndCancelledOrdersByCustomerId(@RequestParam Long customerId) {
        return ResponseEntity.ok(orderService.getCompletedAndCancelledOrdersByCustomerId(customerId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderCreateResponseDto> getOrderDetailsById(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderDetailsById(orderId));
    }

}
