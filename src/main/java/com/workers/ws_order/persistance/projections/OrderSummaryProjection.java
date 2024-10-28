package com.workers.ws_order.persistance.projections;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderSummaryProjection {
    private Long id;
    private Long customerId;
    private Integer category;
    private BigDecimal amount;
    private String shortDescription;
    private String detailedDescription;
    private String status;
    private LocalDateTime createdAt;
}
