package com.workers.ws_order.persistance.repository;

import com.workers.ws_order.persistance.entity.BidEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BidRepository extends JpaRepository<BidEntity, Long> {
}
