package com.workers.ws_order.persistance.repository.custom.impl;

import com.workers.ws_order.rest.inbound.dto.common.model.pagination.enums.SortOrder;
import com.workers.ws_order.rest.inbound.dto.common.model.pagination.Sort;
import com.workers.ws_order.persistance.projections.OrderSummaryProjection;
import com.workers.ws_order.persistance.enums.BidStatus;
import com.workers.ws_order.persistance.enums.OrderStatus;
import com.workers.ws_order.persistance.repository.custom.OrderPageableCustomRepository;
import com.workers.ws_order.rest.inbound.dto.getorder.OrderSummaryRequestDto;
import com.workers.ws_order.rest.inbound.dto.getorder.OrderSummaryRequestFilter;
import com.workers.ws_order.rest.inbound.dto.getorder.enums.OrderSummarySortBy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.OrderField;
import org.jooq.SelectSelectStep;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@Slf4j
@Repository
@RequiredArgsConstructor
public class OrderPageableCustomRepositoryImpl implements OrderPageableCustomRepository {

    private final DSLContext dslContext;

    private final Field<Long> orderSerialField = field("o.id", Long.class);
    private final Field<Long> customerSerialField = field("o.customer_id", Long.class);
    private final Field<Integer> orderCategoryField = field("o.category", Integer.class);
    private final Field<BigDecimal> orderAmountField = field("o.amount", BigDecimal.class);
    private final Field<String> shortDescriptionField = field("o.short_description", String.class);
    private final Field<String> detailedDescriptionField = field("o.detailed_description", String.class);
    private final Field<String> orderStatusField = field("o.status", String.class);
    private final Field<LocalDateTime> orderCreatedAtField = field("o.created_at", LocalDateTime.class);
    private final Field<Long> bidOrderSerialField = field("b.order_id", Long.class);
    private final Field<String> bidOrderStatusField = field("b.status", String.class);

    private final int ORDER_LIMIT_DATE = 3;

    /**
     * Этот метод подсчитывает общее количество записей, соответствующих заданным фильтрам.
     * @param filter
     * @return Результат используется для определения общего количества страниц или элементов для пагинации.
     */
    @Override
    @SuppressWarnings("unchecked")
    public long getRecordsCount(OrderSummaryRequestFilter filter) {
        return dslContext
                .selectCount()
                .from(table("ws-order-management.ws01_order").as("o"))
                .where(buildFilterSection(filter))
                .fetchOne(0, Long.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<OrderSummaryProjection> getOrderListByFilter(OrderSummaryRequestDto request) {
        var query = buildSelectSection()
                .from(buildOrderTable(request.filter()))
                .orderBy(buildOrderSection(request.pageable().getSort()))
                .limit((int) request.pageable().getItemsLimit())
                .offset((int) request.pageable().getOffset());

        return query.fetchInto(OrderSummaryProjection.class);
    }

    /**
     *  Определяет, какие поля будут выбраны в запросе.
     * @return
     */
    private SelectSelectStep<?> buildSelectSection() {
        return dslContext.select(
                field("id", Long.class),
                field("customer_id", Long.class),
                field("category", Integer.class),
                field("amount", BigDecimal.class),
                field("short_description", String.class),
                field("detailed_description", String.class),
                field("status", String.class),
                field("created_at", LocalDateTime.class)
        );
    }

    /**
     * Создает подзапрос с условиями фильтрации.
     * @param filter
     * @return
     */
    private Table<?> buildOrderTable(OrderSummaryRequestFilter filter) {
        return dslContext
                .select(
                        orderSerialField.as("id"),
                        customerSerialField.as("customer_id"),
                        orderCategoryField.as("category"),
                        orderAmountField.as("amount"),
                        shortDescriptionField.as("short_description"),
                        detailedDescriptionField.as("detailed_description"),
                        orderStatusField.as("status"),
                        orderCreatedAtField.as("created_at")
                )
                .from(table("ws-order-management.ws01_order").as("o"))
                .where(buildFilterSection(filter))
                .asTable("orderList");
    }

    private Condition buildFilterSection(OrderSummaryRequestFilter filter) {

        List<Condition> conditions = new ArrayList<>();

        // Фильтрация по категории
        if (CollectionUtils.isNotEmpty(filter.category())) {
            conditions.add(orderCategoryField.in(filter.category()));
        }

        // Фильтрация по сумме
        if (filter.amountFrom() != null) {
            conditions.add(orderAmountField.greaterOrEqual(filter.amountFrom()));
        }
        if (filter.amountTo() != null) {
            conditions.add(orderAmountField.lessOrEqual(filter.amountTo()));
        }

        // Фильтрация по дате
        if (filter.createdDateFrom() != null) {
            conditions.add(orderCreatedAtField.greaterOrEqual(filter.createdDateFrom().atStartOfDay()));
        }
        if (filter.createdDateTo() != null) {
            conditions.add(orderCreatedAtField.lessThan(filter.createdDateTo().plusDays(1).atStartOfDay()));
        }

        // Если не задан пользовательский фильтр по дате, ограничиваем выборку 3 месяца
        if (filter.createdDateFrom() == null && filter.createdDateTo() == null) {
            conditions.add(orderCreatedAtField.greaterOrEqual(LocalDate.now().minusMonths(ORDER_LIMIT_DATE).atStartOfDay()));
        }

        // Фильтрация заказа ТОЛЬКО по статусу NEW
        conditions.add(orderStatusField.eq(OrderStatus.NEW.name()));

        // Исключаем заказы, у которых есть хотя бы один bid со статусом ACCEPTED
        Condition noAcceptedBidsCondition = DSL.notExists(
                DSL.selectOne()
                        .from(table("ws-order-management.ws01_bid").as("b"))
                        .where(
                                bidOrderSerialField.eq(orderSerialField)
                                        .and(bidOrderStatusField.eq(BidStatus.ACCEPTED.name()))
                        )
        );

        conditions.add(noAcceptedBidsCondition);

        return DSL.and(conditions);
    }

    /**
     * Определяет порядок сортировки результатов.
     * @param sort
     * @return
     */
    private Collection<OrderField<?>> buildOrderSection(Sort<OrderSummarySortBy> sort) {
        OrderField<?> resultSortField = orderCreatedAtField.desc();
        if (sort != null && sort.getSortBy() != null) {
            Field<?> field = getOrderFieldName(sort);
            if (field != null) {
                resultSortField = sort.getSortOrder() == SortOrder.DESC ? field.desc() : field.asc();
            }
        }
        return List.of(resultSortField, orderSerialField.desc());
    }

    private Field<?> getOrderFieldName(Sort<OrderSummarySortBy> sort) {
        switch (sort.getSortBy()) {
            case ORDER_DATE:
                return orderCreatedAtField;
            case AMOUNT:
                return orderAmountField;
            default:
                return null;
        }
    }
}
