package com.workers.ws_order.persistance.repository.custom;

import com.workers.ws_order.persistance.projections.OrderSummaryProjection;
import com.workers.ws_order.rest.inbound.dto.getorder.OrderSummaryRequestDto;
import com.workers.ws_order.rest.inbound.dto.getorder.OrderSummaryRequestFilter;

import java.util.List;

public interface OrderPageableCustomRepository {

    long getRecordsCount(OrderSummaryRequestFilter filter);

    List<OrderSummaryProjection> getOrderListByFilter(OrderSummaryRequestDto request);
}
