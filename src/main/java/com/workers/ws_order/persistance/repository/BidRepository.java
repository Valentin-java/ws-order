package com.workers.ws_order.persistance.repository;

import com.workers.ws_order.persistance.entity.BidEntity;
import com.workers.ws_order.persistance.enums.BidStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<BidEntity, Long> {
    List<BidEntity> findByOrderId(Long orderId);
    BidEntity findFirstByOrderIdAndStatus(Long orderId, BidStatus status);
    List<BidEntity> findByOrderIdAndStatus(Long orderId, BidStatus status);
    boolean existsByOrderIdAndStatus(Long orderId, BidStatus status);
}
