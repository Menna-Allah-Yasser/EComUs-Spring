package org.iti.ecomus.repository;

import org.iti.ecomus.dto.stats.*;
import org.iti.ecomus.entity.Order;
import org.iti.ecomus.entity.OrderDetails;
import org.iti.ecomus.entity.Product;
import org.iti.ecomus.paging.SearchRepository;
import org.iti.ecomus.specification.OrderSpecification;
import org.iti.ecomus.specification.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface OrderRepo extends JpaRepository<Order, Long>, SearchRepository<Order, Long> {

    List<Order> findByUser_UserId(Long userId);

    boolean existsByOrderIdAndUser_UserId(Long orderId, Long userId);


    // Statistics Methods
    @Query("""
        SELECT COUNT(o) as totalOrders,
               COALESCE(SUM(o.price), 0) as totalRevenue,
               COUNT(CASE WHEN o.status = 'PENDING' THEN 1 END) as pendingOrders,
               COUNT(CASE WHEN o.status = 'DELIVERED' THEN 1 END) as completedOrders,
               COUNT(CASE WHEN o.status = 'CANCELLED' THEN 1 END) as cancelledOrders,
               COALESCE(AVG(o.price), 0) as averageOrderValue
        FROM Order o
    """)
    OrderStatsProjection getOrderStatistics();

    @Query(value = """
    SELECT CAST(o.date AS DATE) as orderDate,
           COUNT(*) as orderCount,
           COALESCE(SUM(o.price), 0) as dailyRevenue
    FROM orders o
    WHERE o.date >= :startDate
    GROUP BY CAST(o.date AS DATE)
    ORDER BY orderDate DESC
""", nativeQuery = true)
    List<DailyOrderStatsProjection> getDailyOrderStats(@Param("startDate") LocalDate startDate);

    @Query("""
        SELECT FUNCTION('YEAR', o.date) as year,
               FUNCTION('MONTH', o.date) as month,
               COUNT(o) as orderCount,
               COALESCE(SUM(o.price), 0) as monthlyRevenue
        FROM Order o
        WHERE o.date >= :startDate
        GROUP BY FUNCTION('YEAR', o.date), FUNCTION('MONTH', o.date)
        ORDER BY FUNCTION('YEAR', o.date) DESC, FUNCTION('MONTH', o.date) DESC
    """)
    List<MonthlyOrderStatsProjection> getMonthlyOrderStats(@Param("startDate") LocalDate startDate);

    @Query("""
        SELECT o.status as status,
               COUNT(o) as count,
               COALESCE(SUM(o.price), 0) as totalAmount
        FROM Order o
        GROUP BY o.status
    """)
    List<OrderStatusDistributionProjection> getOrderStatusDistribution();

    @Query(value = """
    SELECT o.payType as paymentType,
           COUNT(*) as count,
           COALESCE(SUM(o.price), 0) as totalAmount,
           CAST(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM orders) AS DOUBLE) as percentage
    FROM orders o
    GROUP BY o.payType
""", nativeQuery = true)
    List<PaymentTypeStatsProjection> getPaymentTypeStatistics();

    @Query(value = """
    SELECT CAST(o.date AS DATE) as periodDate,
           COALESCE(SUM(o.price), 0) as revenue,
           COUNT(*) as orderCount
    FROM orders o
    WHERE o.date BETWEEN :startDate AND :endDate
    GROUP BY CAST(o.date AS DATE)
    ORDER BY periodDate DESC
""", nativeQuery = true)
    List<RevenueByPeriodProjection> getRevenueByPeriod(@Param("startDate") LocalDate startDate,
                                                       @Param("endDate") LocalDate endDate);

    @Query(value = """
    SELECT o.orderId as orderId,
           u.userName as userName,
           u.email as userEmail,
           o.price as orderTotal,
           CAST(o.date AS DATE) as orderDate,
           o.status as status
    FROM orders o
    JOIN user u ON o.userId = u.userId
    ORDER BY o.date DESC
    LIMIT :limit
""", nativeQuery = true)
    List<RecentOrderProjection> getRecentOrders(@Param("limit") int limit);

    @Query("""
        SELECT o.address as city,
               '' as state,
               COUNT(o) as orderCount,
               COALESCE(SUM(o.price), 0) as totalRevenue
        FROM Order o
        GROUP BY o.address
        ORDER BY orderCount DESC
    """)
    List<OrdersByLocationProjection> getOrdersByLocation();

    @Query(value = """
    SELECT COUNT(*) as totalUsers,
           COUNT(*) as totalOrders,
           COALESCE(SUM(o.price), 0) as totalRevenue,
           COUNT(CASE WHEN o.status = 'PENDING' THEN 1 END) as pendingOrders,
           COUNT(CASE WHEN CAST(o.date AS DATE) = CURRENT_DATE THEN 1 END) as todayOrders,
           COALESCE(SUM(CASE WHEN CAST(o.date AS DATE) = CURRENT_DATE THEN o.price ELSE 0 END), 0) as todayRevenue,
           0 as newCustomersThisMonth,
           0.0 as revenueGrowthRate
    FROM orders o
""", nativeQuery = true)
    DashboardSummaryProjection getDashboardSummary();

    @Override
    default Specification<Order> getFiltersSpecification(String keyword, Map<String, Object> searchParams) {
        return OrderSpecification.build(keyword, searchParams);
    }
}
