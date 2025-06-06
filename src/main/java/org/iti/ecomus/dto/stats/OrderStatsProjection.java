package org.iti.ecomus.dto.stats;

import java.math.BigDecimal;

public interface OrderStatsProjection {
    Long getTotalOrders();
    BigDecimal getTotalRevenue();
    Long getPendingOrders();
    Long getCompletedOrders();
    Long getCancelledOrders();
    BigDecimal getAverageOrderValue();
}