package com.workers.ws_order.persistance.repository;

import com.workers.ws_order.persistance.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository  extends JpaRepository<OrderEntity, Long> {
}
