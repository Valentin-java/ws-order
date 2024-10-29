package com.workers.ws_order.bussines.order.worker.service;

import com.workers.ws_order.bussines.order.worker.interfaces.OrderWorkerService;
import com.workers.ws_order.persistance.entity.BidEntity;
import com.workers.ws_order.persistance.entity.OrderEntity;
import com.workers.ws_order.persistance.enums.BidStatus;
import com.workers.ws_order.persistance.enums.OrderStatus;
import com.workers.ws_order.persistance.projections.OrderSummaryProjection;
import com.workers.ws_order.persistance.repository.BidRepository;
import com.workers.ws_order.persistance.repository.OrderRepository;
import com.workers.ws_order.persistance.repository.custom.OrderPageableCustomRepository;
import com.workers.ws_order.rest.inbound.dto.common.model.pagination.PageDomain;
import com.workers.ws_order.rest.inbound.dto.getorder.OrderSummaryRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderWorkerServiceImpl implements OrderWorkerService {

    private final BidRepository bidRepository;
    private final OrderRepository orderRepository;
    private final OrderPageableCustomRepository orderPageableCustomRepository;

    @Override
    @Transactional(readOnly = true)
    public PageDomain<OrderSummaryProjection> getOrderByFilter(OrderSummaryRequestDto requestDto)
            throws ExecutionException, InterruptedException {
        log.info("Fetching available orders for specialist ID: {}", requestDto);


        var futureRecordCount = CompletableFuture.supplyAsync(
                () -> orderPageableCustomRepository.getRecordsCount(requestDto.filter()));

        var orderList = orderPageableCustomRepository.getOrderListByFilter(requestDto);

        return new PageDomain<>(
                requestDto.pageable().getOffset(),
                requestDto.pageable().getItemsLimit(),
                futureRecordCount.get(),
                orderList);
    }

    @Override
    @Transactional
    public void completeOrder(Long orderId, Long specialistId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Order not found with ID: " + orderId));

        BidEntity acceptedBid = bidRepository.findFirstByOrderIdAndStatus(orderId, BidStatus.ACCEPTED);
        if (acceptedBid == null || !acceptedBid.getSpecialistId().equals(specialistId)) {
            throw new ResponseStatusException(BAD_REQUEST, "Only the specialist with the accepted bid can complete the order");
        }

        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);
        log.info("Order with ID: {} has been marked as completed by specialist ID: {}", orderId, specialistId);
    }
}
