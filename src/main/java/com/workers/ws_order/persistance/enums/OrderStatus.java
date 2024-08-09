package com.workers.ws_order.persistance.enums;

public enum OrderStatus {
    NEW,        // Новый заказ
    IN_PROGRESS, // Заказ в процессе выполнения
    COMPLETED,  // Заказ выполнен
    CANCELLED   // Заказ отменен
}
