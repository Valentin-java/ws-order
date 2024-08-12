package com.workers.ws_order.rest.inbound.dto.createmsg;

public record MessageCreateRequestDto(
        Long senderId,
        String content
) {
}
