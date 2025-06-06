package org.iti.ecomus.dto.stats;

import java.math.BigDecimal;

public interface ProductSalesStatsProjection {
    Long getProductId();
    String getProductName();
    Long getOrderCount();
    Long getTotalQuantity();
    BigDecimal getTotalRevenue();
    BigDecimal getAverageOrderQuantity();
}