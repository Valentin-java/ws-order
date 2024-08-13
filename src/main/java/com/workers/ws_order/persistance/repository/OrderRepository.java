package com.workers.ws_order.persistance.repository;

import com.workers.ws_order.persistance.entity.OrderEntity;
import com.workers.ws_order.persistance.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository  extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByCustomerIdAndStatus(Long customerId, OrderStatus status);

    List<OrderEntity> findByCustomerIdAndStatusIn(Long customerId, List<OrderStatus> statuses);

    List<OrderEntity> findAllByStatus(OrderStatus status);
}
