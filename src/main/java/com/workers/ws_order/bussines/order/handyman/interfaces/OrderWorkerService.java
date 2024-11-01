package com.workers.ws_order.bussines.order.handyman.interfaces;

import com.workers.ws_order.persistance.projections.OrderSummaryProjection;
import com.workers.ws_order.rest.inbound.dto.common.model.pagination.PageDomain;
import com.workers.ws_order.rest.inbound.dto.getorder.OrderSummaryRequestDto;

import java.util.concurrent.ExecutionException;

public interface OrderWorkerService {

    PageDomain<OrderSummaryProjection> getOrderByFilter(OrderSummaryRequestDto requestDto) throws ExecutionException, InterruptedException;

    void completeOrder(Long orderId, Long specialistId);
}
