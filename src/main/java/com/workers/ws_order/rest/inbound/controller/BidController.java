package com.workers.ws_order.rest.inbound.controller;

import com.workers.ws_order.bussines.bid.interfaces.BidService;
import com.workers.ws_order.rest.inbound.dto.acceptbid.BidChangeStatusRequest;
import com.workers.ws_order.rest.inbound.dto.createbid.BidCreateRequestDto;
import com.workers.ws_order.rest.inbound.dto.createbid.BidCreateResponseDto;
import com.workers.ws_order.rest.inbound.dto.getbid.BidSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/bid")
public class BidController {
// декомпозировать для заказчика/исполнителя // детальный бид для формы
    private final BidService service;

    @PostMapping("/create")
    public ResponseEntity<BidCreateResponseDto> createBid(@RequestBody BidCreateRequestDto requestDto) {
        return ResponseEntity.ok(service.createBid(requestDto));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<BidSummaryDto>> getBidsByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(service.getBidsByOrderId(orderId));
    }

    @PostMapping("/accept")
    public ResponseEntity<Void> acceptBid(@RequestBody BidChangeStatusRequest request) {
        service.acceptBid(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reject")
    public ResponseEntity<Void> rejectBid(@RequestBody BidChangeStatusRequest request) {
        service.rejectBid(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/specialist/{specialistId}")
    public ResponseEntity<List<BidSummaryDto>> getBidsBySpecialistId(@PathVariable Long specialistId) {
        return ResponseEntity.ok(service.getBidsBySpecialistId(specialistId));
    }

    @PostMapping("/cancel")
    public ResponseEntity<Void> cancelBid(@RequestBody BidChangeStatusRequest request) {
        service.cancelBid(request);
        return ResponseEntity.ok().build();
    }
}
