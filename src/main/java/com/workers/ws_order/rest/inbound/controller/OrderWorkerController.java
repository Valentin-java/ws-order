package com.workers.ws_order.rest.inbound.controller;

import com.workers.ws_order.bussines.order.worker.interfaces.OrderWorkerService;
import com.workers.ws_order.rest.inbound.dto.common.model.pagination.PageDomain;
import com.workers.ws_order.persistance.projections.OrderSummaryProjection;
import com.workers.ws_order.rest.inbound.dto.getorder.OrderSummaryRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/order/worker")
public class OrderWorkerController {

    private final OrderWorkerService service;

    @PostMapping("/available")
    public ResponseEntity<PageDomain<OrderSummaryProjection>> getOrderListByFilter(@RequestBody OrderSummaryRequestDto requestDto) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(service.getOrderByFilter(requestDto));
    }

    // ручка для заказа в статусе inProgress

    @PostMapping("/{orderId}/complete")
    public ResponseEntity<?> completeOrder(@PathVariable Long orderId, @RequestParam Long specialistId) {
        service.completeOrder(orderId, specialistId);
        return ResponseEntity.ok().build();
    }
}
