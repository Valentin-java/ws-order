package com.workers.ws_order.bussines.bid.interfaces;

import com.workers.ws_order.rest.inbound.dto.createbid.BidCreateRequestDto;
import com.workers.ws_order.rest.inbound.dto.createbid.BidCreateResponseDto;
import com.workers.ws_order.rest.inbound.dto.getbid.BidSummaryDto;

import java.util.List;

public interface BidService {

    BidCreateResponseDto createBid(BidCreateRequestDto requestDto);

    List<BidSummaryDto> getBidsByOrderId(Long orderId);

    void acceptBid(Long bidId);

    void rejectBid(Long bidId);
}
