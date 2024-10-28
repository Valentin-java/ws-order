package com.workers.ws_order.rest.inbound.dto.common.model.pagination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Хранит информацию о текущей странице, размере страницы, общем количестве записей и списке элементов на текущей странице.
 * @param <T>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageDomain<T> {

    /**
     * Определяет смещение от начала результата (сколько записей пропустить).
     */
    private long offset;

    /**
     * Определяет максимальное количество записей для выборки.
     */
    private long itemsLimit;

    /**
     * Общее колечиство записей.
     */
    private long itemsTotal;

    /**
     * Список элементов на текущей странице.
     */
    private List<T> data;
}
