package com.workers.ws_order.rest.inbound.dto.common.model;

public record FileDto(
        byte[] bytes,
        String name,
        String originalFilename,
        String contentType
) {
}
