package com.workers.ws_order.rest.inbound.dto.getorder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record OrderSummaryRequestFilter(
        List<Integer> category,
        BigDecimal amountFrom,
        BigDecimal amountTo,
        LocalDate createdDateFrom,
        LocalDate createdDateTo
) {
}
