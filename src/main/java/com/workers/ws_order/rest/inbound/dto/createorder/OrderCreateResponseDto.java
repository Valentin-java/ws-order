package com.workers.ws_order.rest.inbound.dto.createorder;

import java.time.LocalDateTime;
import java.util.List;

public record OrderCreateResponseDto(
        Long orderId,
        Long customerId,
        String category,
        String shortDescription,
        String detailedDescription,
        String status,
        LocalDateTime createdAt,
        List<byte[]> photoData
) {
}
