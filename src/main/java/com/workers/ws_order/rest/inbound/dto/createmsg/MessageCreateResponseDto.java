package com.workers.ws_order.rest.inbound.dto.createmsg;

import java.time.LocalDateTime;

public record MessageCreateResponseDto(
        Long messageId,
        Long bidId,
        Long senderId,
        String content,
        LocalDateTime timestamp
) {
}
