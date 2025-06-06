package org.iti.ecomus.service.impl;

import org.iti.ecomus.dto.stats.*;
import org.iti.ecomus.repository.OrderDetailsRepo;
import org.iti.ecomus.repository.OrderRepo;
import org.iti.ecomus.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class StatisticsService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private OrderDetailsRepo orderDetailsRepo;

    // Dashboard Summary
    public DashboardSummaryProjection getDashboardSummary() {
        return orderRepo.getDashboardSummary();
    }

    public List<UserRoleDistributionProjection> getUserRoleDistribution() {
        return userRepo.getUserRoleDistribution();
    }

    public List<TopCustomersProjection> getTopCustomers(int limit) {
        return userRepo.getTopCustomers(limit);
    }

    public List<CustomerOrderStatsProjection> getCustomerOrderStatistics() {
        return userRepo.getCustomerOrderStatistics();
    }

    // Order Statistics
    public OrderStatsProjection getOrderStatistics() {
        return orderRepo.getOrderStatistics();
    }

    public List<DailyOrderStatsProjection> getDailyOrderStats(int days) {
        LocalDate startDate = LocalDate.now().minus(days, ChronoUnit.DAYS);
        return orderRepo.getDailyOrderStats(startDate);
    }

    public List<MonthlyOrderStatsProjection> getMonthlyOrderStats(int months) {
        LocalDate startDate = LocalDate.now().minus(months, ChronoUnit.MONTHS);
        return orderRepo.getMonthlyOrderStats(startDate);
    }

    public List<OrderStatusDistributionProjection> getOrderStatusDistribution() {
        return orderRepo.getOrderStatusDistribution();
    }

    public List<PaymentTypeStatsProjection> getPaymentTypeStatistics() {
        return orderRepo.getPaymentTypeStatistics();
    }

    public List<RevenueByPeriodProjection> getRevenueByPeriod(LocalDate startDate, LocalDate endDate) {
        return orderRepo.getRevenueByPeriod(startDate, endDate);
    }

    public List<RecentOrderProjection> getRecentOrders(int limit) {
        return orderRepo.getRecentOrders(limit);
    }

    public List<OrdersByLocationProjection> getOrdersByLocation() {
        return orderRepo.getOrdersByLocation();
    }

    // Product Statistics
    public List<TopSellingProductProjection> getTopSellingProducts(int limit) {
        return orderDetailsRepo.getTopSellingProducts(limit);
    }

    public List<ProductSalesStatsProjection> getProductSalesStatistics() {
        return orderDetailsRepo.getProductSalesStatistics();
    }

    public OrderDetailsStatsProjection getOrderDetailsStatistics() {
        return orderDetailsRepo.getOrderDetailsStatistics();
    }

    // Convenience methods for common dashboard needs
    public List<DailyOrderStatsProjection> getLastWeekOrderStats() {
        return getDailyOrderStats(7);
    }

    public List<DailyOrderStatsProjection> getLastMonthOrderStats() {
        return getDailyOrderStats(30);
    }

    public List<MonthlyOrderStatsProjection> getLastYearOrderStats() {
        return getMonthlyOrderStats(12);
    }



    public List<TopSellingProductProjection> getTop10Products() {
        return getTopSellingProducts(10);
    }

    public List<TopCustomersProjection> getTop10Customers() {
        return getTopCustomers(10);
    }

    public List<RecentOrderProjection> getLast10Orders() {
        return getRecentOrders(10);
    }
}