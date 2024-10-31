package com.workers.ws_order.rest.inbound.dto.getorder;

import com.workers.ws_order.rest.inbound.dto.common.model.pagination.Pageable;
import com.workers.ws_order.rest.inbound.dto.common.model.pagination.enums.OrderSummarySortBy;

public record OrderSummaryRequestDto(
        Long specialistId,
        OrderSummaryRequestFilter filter,
        Pageable<OrderSummarySortBy> pageable
) {
}
