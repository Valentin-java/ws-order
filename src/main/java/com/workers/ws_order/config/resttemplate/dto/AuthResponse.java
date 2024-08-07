package com.workers.ws_order.config.resttemplate.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {
}
