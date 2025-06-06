package org.iti.ecomus.dto.stats;

import java.math.BigDecimal;

public interface PaymentTypeStatsProjection {
    String getPaymentType();
    Long getCount();
    BigDecimal getTotalAmount();
    Double getPercentage();
}