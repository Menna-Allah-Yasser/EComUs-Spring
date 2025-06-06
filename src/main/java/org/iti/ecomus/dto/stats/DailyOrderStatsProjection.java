package org.iti.ecomus.dto.stats;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface DailyOrderStatsProjection {
    LocalDate getOrderDate();
    Long getOrderCount();
    BigDecimal getDailyRevenue();
}