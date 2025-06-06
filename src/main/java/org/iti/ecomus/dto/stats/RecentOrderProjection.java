package org.iti.ecomus.dto.stats;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface RecentOrderProjection {
    Long getOrderId();
    String getUserName();
    String getUserEmail();
    BigDecimal getOrderTotal();
    LocalDate getOrderDate();
    String getStatus();
}