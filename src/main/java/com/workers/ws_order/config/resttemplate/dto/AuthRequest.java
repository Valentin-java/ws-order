package com.workers.ws_order.config.resttemplate.dto;

public record AuthRequest(
        String username,
        String password,
        Boolean otp
) {
}
