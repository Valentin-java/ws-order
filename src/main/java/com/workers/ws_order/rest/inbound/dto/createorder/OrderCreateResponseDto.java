package com.workers.ws_order.rest.inbound.dto.createorder;

import java.time.LocalDateTime;

public record OrderCreateResponseDto(
        Long orderId,
        Long customerId,
        String category,
        String shortDescription,
        String detailedDescription,
        String status,
        LocalDateTime createdAt
) {
}
