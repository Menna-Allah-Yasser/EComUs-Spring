package org.iti.ecomus.dto.stats;

import java.math.BigDecimal;

public interface OrderDetailsStatsProjection {
    BigDecimal getAverageItemsPerOrder();
    Long getTotalItemsSold();
    BigDecimal getAverageItemPrice();
}