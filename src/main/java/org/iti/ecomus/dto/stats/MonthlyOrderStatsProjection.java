package org.iti.ecomus.dto.stats;

import java.math.BigDecimal;

public interface MonthlyOrderStatsProjection {
    Integer getYear();
    Integer getMonth();
    Long getOrderCount();
    BigDecimal getMonthlyRevenue();
}