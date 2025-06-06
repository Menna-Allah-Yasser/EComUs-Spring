package org.iti.ecomus.dto.stats;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface RecentCustomerProjection {
    Long getUserId();
    String getUserName();
    String getEmail();
    LocalDate getRegistrationDate();
    Long getOrderCount();
    BigDecimal getTotalSpent();
}