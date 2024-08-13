package com.workers.ws_order.rest.inbound.controller;

import com.workers.ws_order.bussines.bid.interfaces.BidService;
import com.workers.ws_order.rest.inbound.dto.createbid.BidCreateRequestDto;
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

    private final BidService bidService;

    @PostMapping("/create")
    public ResponseEntity<?> createBid(@RequestBody BidCreateRequestDto requestDto) {
        return ResponseEntity.ok(bidService.createBid(requestDto));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<BidSummaryDto>> getBidsByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(bidService.getBidsByOrderId(orderId));
    }

    @PostMapping("/{bidId}/accept")
    public ResponseEntity<?> acceptBid(@PathVariable Long bidId) {
        bidService.acceptBid(bidId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{bidId}/reject")
    public ResponseEntity<?> rejectBid(@PathVariable Long bidId) {
        bidService.rejectBid(bidId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/specialist/{specialistId}")
    public ResponseEntity<List<BidSummaryDto>> getBidsBySpecialistId(@PathVariable Long specialistId) {
        return ResponseEntity.ok(bidService.getBidsBySpecialistId(specialistId));
    }

    @PostMapping("/{bidId}/cancel")
    public ResponseEntity<?> cancelBid(@PathVariable Long bidId) {
        bidService.cancelBid(bidId);
        return ResponseEntity.ok().build();
    }
}
