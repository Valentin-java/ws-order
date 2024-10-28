package com.workers.ws_order.rest.inbound.dto.getorder;

import com.workers.ws_order.rest.inbound.dto.common.model.pagination.Pageable;

public record OrderSummaryRequestDto(
        Long specialistId,
        OrderSummaryRequestFilter filter,
        Pageable pageable
) {
}
