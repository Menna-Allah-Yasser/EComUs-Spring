package org.iti.ecomus.dto.stats;

import java.math.BigDecimal;

public interface OrderStatusDistributionProjection {
    String getStatus();
    Long getCount();
    BigDecimal getTotalAmount();
}