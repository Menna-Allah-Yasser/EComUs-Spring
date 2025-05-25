package org.iti.ecomus.enums;

public enum OrderStatus {
    PROCESSING,
    COMPLETED,
    CANCELED;

    public static final OrderStatus DEFAULT = PROCESSING;
}
