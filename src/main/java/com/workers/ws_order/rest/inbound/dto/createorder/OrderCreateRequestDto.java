package com.workers.ws_order.rest.inbound.dto.createorder;

import java.util.List;

public record OrderCreateRequestDto(
        Long customerId,
        String category,
        String shortDescription,
        String detailedDescription,
        List<byte[]> photoData
) {
}
