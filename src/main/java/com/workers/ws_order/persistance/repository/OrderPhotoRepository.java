package com.workers.ws_order.persistance.repository;

import com.workers.ws_order.persistance.entity.OrderPhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderPhotoRepository extends JpaRepository<OrderPhotoEntity, Long> {

    List<OrderPhotoEntity> findByOrderId(Long orderId);

    void deleteAllByOrderId(Long orderId);
}
