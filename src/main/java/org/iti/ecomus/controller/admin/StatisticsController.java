package org.iti.ecomus.controller.admin;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.iti.ecomus.dto.stats.*;
import org.iti.ecomus.service.impl.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/statistics")
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Admin - statistics", description = "Admin statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    // Dashboard Summary
    @GetMapping(value = "/dashboard",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DashboardSummaryProjection> getDashboardSummary() {
        return ResponseEntity.ok(statisticsService.getDashboardSummary());
    }

    // User Statistics


    @GetMapping(value = "/users/role-distribution",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserRoleDistributionProjection>> getUserRoleDistribution() {
        return ResponseEntity.ok(statisticsService.getUserRoleDistribution());
    }

    @GetMapping(value = "/users/top-customers",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TopCustomersProjection>> getTopCustomers(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(statisticsService.getTopCustomers(limit));
    }

    @GetMapping(value = "/users/customer-stats",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CustomerOrderStatsProjection>> getCustomerOrderStatistics() {
        return ResponseEntity.ok(statisticsService.getCustomerOrderStatistics());
    }

    // Order Statistics
    @GetMapping(value = "/orders/summary",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderStatsProjection> getOrderStatistics() {
        return ResponseEntity.ok(statisticsService.getOrderStatistics());
    }

    @GetMapping(value = "/orders/daily-stats",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DailyOrderStatsProjection>> getDailyOrderStats(
            @RequestParam(defaultValue = "30") int days) {
        return ResponseEntity.ok(statisticsService.getDailyOrderStats(days));
    }

    @GetMapping(value = "/orders/monthly-stats",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MonthlyOrderStatsProjection>> getMonthlyOrderStats(
            @RequestParam(defaultValue = "12") int months) {
        return ResponseEntity.ok(statisticsService.getMonthlyOrderStats(months));
    }

    @GetMapping(value = "/orders/status-distribution",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderStatusDistributionProjection>> getOrderStatusDistribution() {
        return ResponseEntity.ok(statisticsService.getOrderStatusDistribution());
    }

    @GetMapping(value = "/orders/payment-stats",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PaymentTypeStatsProjection>> getPaymentTypeStatistics() {
        return ResponseEntity.ok(statisticsService.getPaymentTypeStatistics());
    }

    @GetMapping(value = "/orders/revenue-by-period",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RevenueByPeriodProjection>> getRevenueByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(statisticsService.getRevenueByPeriod(startDate, endDate));
    }

    @GetMapping(value = "/orders/recent",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RecentOrderProjection>> getRecentOrders(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(statisticsService.getRecentOrders(limit));
    }

    @GetMapping(value = "/orders/by-location",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrdersByLocationProjection>> getOrdersByLocation() {
        return ResponseEntity.ok(statisticsService.getOrdersByLocation());
    }

    // Product Statistics
    @GetMapping(value = "/products/top-selling",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TopSellingProductProjection>> getTopSellingProducts(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(statisticsService.getTopSellingProducts(limit));
    }

    @GetMapping(value = "/products/sales-stats",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductSalesStatsProjection>> getProductSalesStatistics() {
        return ResponseEntity.ok(statisticsService.getProductSalesStatistics());
    }

    @GetMapping(value = "/products/order-details-stats",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDetailsStatsProjection> getOrderDetailsStatistics() {
        return ResponseEntity.ok(statisticsService.getOrderDetailsStatistics());
    }

    // Convenience endpoints for common dashboard widgets
    @GetMapping(value = "/quick/last-week-orders",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DailyOrderStatsProjection>> getLastWeekOrderStats() {
        return ResponseEntity.ok(statisticsService.getLastWeekOrderStats());
    }

    @GetMapping(value = "/quick/last-month-orders",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DailyOrderStatsProjection>> getLastMonthOrderStats() {
        return ResponseEntity.ok(statisticsService.getLastMonthOrderStats());
    }

    @GetMapping(value = "/quick/last-year-orders",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MonthlyOrderStatsProjection>> getLastYearOrderStats() {
        return ResponseEntity.ok(statisticsService.getLastYearOrderStats());
    }

    @GetMapping(value = "/quick/top-10-products",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TopSellingProductProjection>> getTop10Products() {
        return ResponseEntity.ok(statisticsService.getTop10Products());
    }

    @GetMapping(value = "/quick/top-10-customers",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TopCustomersProjection>> getTop10Customers() {
        return ResponseEntity.ok(statisticsService.getTop10Customers());
    }

    @GetMapping(value = "/quick/recent-10-orders",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RecentOrderProjection>> getLast10Orders() {
        return ResponseEntity.ok(statisticsService.getLast10Orders());
    }
}