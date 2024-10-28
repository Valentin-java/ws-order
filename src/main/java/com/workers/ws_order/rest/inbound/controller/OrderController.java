package com.workers.ws_order.rest.inbound.controller;

import com.workers.ws_order.rest.inbound.dto.common.model.pagination.PageDomain;
import com.workers.ws_order.bussines.order.interfaces.OrderService;
import com.workers.ws_order.persistance.projections.OrderSummaryProjection;
import com.workers.ws_order.rest.inbound.dto.createorder.OrderCreateRequestDto;
import com.workers.ws_order.rest.inbound.dto.createorder.OrderCreateResponseDto;
import com.workers.ws_order.rest.inbound.dto.getorder.OrderSummaryDto;
import com.workers.ws_order.rest.inbound.dto.getorder.OrderSummaryRequestDto;
import com.workers.ws_order.rest.inbound.dto.updateorder.OrderUpdateRequestDto;
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
import java.util.concurrent.ExecutionException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/order")
public class OrderController {

    private final OrderService service;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody OrderCreateRequestDto requestDto) {
        return ResponseEntity.ok(service.createOrder(requestDto));
    }

    @GetMapping("/active")
    public ResponseEntity<List<OrderSummaryDto>> getNewOrdersByCustomerId(@RequestParam Long customerId) {
        return ResponseEntity.ok(service.getNewOrdersByCustomerId(customerId));
    }

    @GetMapping("/archive")
    public ResponseEntity<List<OrderSummaryDto>> getCompletedAndCancelledOrdersByCustomerId(@RequestParam Long customerId) {
        return ResponseEntity.ok(service.getCompletedAndCancelledOrdersByCustomerId(customerId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderCreateResponseDto> getOrderDetailsById(@PathVariable Long orderId) {
        return ResponseEntity.ok(service.getOrderDetailsById(orderId));
    }

    @PostMapping("/update/{orderId}")
    public ResponseEntity<?> updateOrder(@PathVariable Long orderId, @RequestBody OrderUpdateRequestDto requestDto) {
        return ResponseEntity.ok(service.updateOrder(orderId, requestDto));
    }

    @PostMapping("/available")
    public ResponseEntity<PageDomain<OrderSummaryProjection>> getAvailableOrdersForSpecialist(@RequestBody OrderSummaryRequestDto requestDto) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(service.getAvailableOrdersForSpecialist(requestDto));
    }

    @PostMapping("/{orderId}/complete")
    public ResponseEntity<?> completeOrder(@PathVariable Long orderId, @RequestParam Long specialistId) {
        service.completeOrder(orderId, specialistId);
        return ResponseEntity.ok().build();
    }
}
