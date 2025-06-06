package org.iti.ecomus.dto.stats;

public interface UserStatsProjection {
    Long getTotalUsers();
    Long getActiveUsers();
    Long getNewUsersThisMonth();
}