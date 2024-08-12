package com.workers.ws_order.rest.inbound.dto.createbid;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BidCreateResponseDto(
        Long bidId,
        Long orderId,
        Long specialistId,
        BigDecimal price,
        String status,
        LocalDateTime createdAt
) {
}
