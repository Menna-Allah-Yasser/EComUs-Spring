package org.iti.ecomus.dto.stats;

import java.time.LocalDate;

public interface UserRegistrationTrendProjection {
    LocalDate getRegistrationDate();
    Long getUserCount();
}