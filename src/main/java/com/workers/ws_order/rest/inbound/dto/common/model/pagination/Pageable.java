package com.workers.ws_order.rest.inbound.dto.common.model.pagination;

import lombok.Data;

@Data
public class Pageable<T extends Enum<T>> {

    /**
     * Определяет смещение от начала результата (сколько записей пропустить).
     */
    private long offset;

    /**
     * Определяет максимальное количество записей для выборки.
     */
    private long itemsLimit;

    /**
     * Сортировка результатов выполняется по указанным полям и направлениям (ASC/DESC).
     */
    private Sort<T> sort;
}
