package org.iti.ecomus.dto.stats;

import java.math.BigDecimal;

public interface OrdersByLocationProjection {
    String getCity();
    String getState();
    Long getOrderCount();
    BigDecimal getTotalRevenue();
}