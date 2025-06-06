package org.iti.ecomus.dto.stats;

import java.math.BigDecimal;

public interface RevenueComparisonProjection {
    BigDecimal getCurrentPeriodRevenue();
    BigDecimal getPreviousPeriodRevenue();
    Double getGrowthPercentage();
}
