package org.iti.ecomus.dto.stats;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface RevenueByPeriodProjection {
    LocalDate getPeriodDate();
    BigDecimal getRevenue();
    Long getOrderCount();
}