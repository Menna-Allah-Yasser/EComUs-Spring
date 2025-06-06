package org.iti.ecomus.dto.stats;

import java.math.BigDecimal;

public interface DashboardSummaryProjection {
    Long getTotalUsers();
    Long getTotalOrders();
    BigDecimal getTotalRevenue();
    Long getPendingOrders();
    Long getTodayOrders();
    BigDecimal getTodayRevenue();
    Long getNewCustomersThisMonth();
    Double getRevenueGrowthRate();
}