package com.workers.ws_order.rest.inbound.dto.getbid;

import com.workers.ws_order.persistance.enums.BidStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BidSummaryDto(
        Long bidId,
        Long specialistId,
        BigDecimal price,
        BidStatus status,
        LocalDateTime createdAt
) {
}
