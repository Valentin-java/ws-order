package com.workers.ws_order.persistance.repository;

import com.workers.ws_order.persistance.entity.OrderPhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderPhotoRepository extends JpaRepository<OrderPhotoEntity, Long> {
}
