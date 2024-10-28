package com.workers.ws_order.rest.inbound.dto.common.model.pagination;

import com.workers.ws_order.rest.inbound.dto.common.model.pagination.enums.SortOrder;
import lombok.Getter;
import lombok.Setter;

/**
 * Сортировка результатов выполняется по указанным полям и направлениям (ASC/DESC).
 */
@Getter
@Setter
public class Sort<T extends Enum<T>> {
    private SortOrder sortOrder;
    private T sortBy;
}
