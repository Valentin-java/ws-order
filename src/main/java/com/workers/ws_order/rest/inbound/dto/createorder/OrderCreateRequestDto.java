package com.workers.ws_order.rest.inbound.dto.createorder;

import java.math.BigDecimal;

public record OrderCreateRequestDto(
        Long customerId,
        Integer category,
        String shortDescription,
        String detailedDescription,
        BigDecimal amount
) {
}
