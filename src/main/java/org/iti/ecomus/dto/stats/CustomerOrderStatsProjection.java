package org.iti.ecomus.dto.stats;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface CustomerOrderStatsProjection {
    Long getUserId();
    String getUserName();
    String getEmail();
    Long getTotalOrders();
    BigDecimal getTotalSpent();
    BigDecimal getAverageOrderValue();
    LocalDate getLastOrderDate();
}