package org.iti.ecomus.dto.stats;

import java.math.BigDecimal;

public interface TopSellingProductProjection {
    Long getProductId();
    String getProductName();
    Long getTotalQuantitySold();
    BigDecimal getTotalRevenue();
}