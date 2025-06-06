package org.iti.ecomus.dto.stats;

import java.math.BigDecimal;

public interface TopCustomersProjection {
    Long getUserId();
    String getUserName();
    String getEmail();
    BigDecimal getTotalSpent();
    Long getTotalOrders();
}