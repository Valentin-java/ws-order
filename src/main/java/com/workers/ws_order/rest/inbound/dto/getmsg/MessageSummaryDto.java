package com.workers.ws_order.rest.inbound.dto.getmsg;

import java.time.LocalDateTime;

public record MessageSummaryDto(
        Long messageId,
        Long senderId,
        String content,
        LocalDateTime timestamp
) {
}
