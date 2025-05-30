package org.iti.ecomus.enums;

import java.util.EnumSet;
import java.util.Set;

public enum OrderStatus {
    PROCESSING,
    SHIPPED,
    COMPLETED,
    CANCELED;

    public static final OrderStatus DEFAULT = PROCESSING;

    private static final Set<OrderStatus> PROCESSING_ALLOWED_TRANSITIONS =
            EnumSet.of(SHIPPED, CANCELED);
    private static final Set<OrderStatus> SHIPPED_ALLOWED_TRANSITIONS =
            EnumSet.of(COMPLETED, CANCELED);
    private static final Set<OrderStatus> COMPLETED_ALLOWED_TRANSITIONS =
            EnumSet.noneOf(OrderStatus.class); // No transitions allowed from COMPLETED
    private static final Set<OrderStatus> CANCELED_ALLOWED_TRANSITIONS =
            EnumSet.noneOf(OrderStatus.class); // No transitions allowed from CANCELED

    public boolean canTransitionTo(OrderStatus newStatus) {
        switch (this) {
            case PROCESSING:
                return PROCESSING_ALLOWED_TRANSITIONS.contains(newStatus);
            case SHIPPED:
                return SHIPPED_ALLOWED_TRANSITIONS.contains(newStatus);
            case COMPLETED:
                return COMPLETED_ALLOWED_TRANSITIONS.contains(newStatus);
            case CANCELED:
                return CANCELED_ALLOWED_TRANSITIONS.contains(newStatus);
            default:
                return false;
        }
    }

    public Set<OrderStatus> getAllowedTransitions() {
        switch (this) {
            case PROCESSING:
                return PROCESSING_ALLOWED_TRANSITIONS;
            case SHIPPED:
                return SHIPPED_ALLOWED_TRANSITIONS;
            case COMPLETED:
                return COMPLETED_ALLOWED_TRANSITIONS;
            case CANCELED:
                return CANCELED_ALLOWED_TRANSITIONS;
            default:
                return EnumSet.noneOf(OrderStatus.class);
        }
    }
}
