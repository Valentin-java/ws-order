package com.workers.ws_order.rest.inbound.dto.updateorder;

import com.workers.ws_order.persistance.enums.OrderStatus;

import java.util.List;

public record OrderUpdateRequestDto(
        String category,
        String shortDescription,
        String detailedDescription,
        List<byte[]> photoData, // Новый список фотографий
        OrderStatus status      // Возможное изменение статуса заказа
) {
}
