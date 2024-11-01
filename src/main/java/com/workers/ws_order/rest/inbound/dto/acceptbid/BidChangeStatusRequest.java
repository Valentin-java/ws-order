package com.workers.ws_order.rest.inbound.dto.acceptbid;

public record BidChangeStatusRequest(
        Long orderId,
        Long bidId
) {
}
