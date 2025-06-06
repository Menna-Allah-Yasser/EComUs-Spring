package org.iti.ecomus.repository;

import org.iti.ecomus.dto.stats.OrderDetailsStatsProjection;
import org.iti.ecomus.dto.stats.ProductSalesStatsProjection;
import org.iti.ecomus.dto.stats.TopSellingProductProjection;
import org.iti.ecomus.entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderDetailsRepo extends JpaRepository<OrderDetails, Long> {

    List<OrderDetails> findOrderDetailsByOrderOrderId(Long orderId);

    // Statistics Methods
    @Query("""
    SELECT 
        COALESCE(AVG(subquery.totalQuantity), 0) AS averageItemsPerOrder,
        COALESCE(SUM(od.quantity), 0) AS totalItemsSold,
        COALESCE(AVG(od.price), 0) AS averageItemPrice
    FROM OrderDetails od,
         (SELECT o.orderId AS orderIdAlias, SUM(od2.quantity) AS totalQuantity
          FROM Order o
          JOIN OrderDetails od2 ON o.orderId = od2.order.orderId
          GROUP BY o.orderId) subquery
""")
    OrderDetailsStatsProjection getOrderDetailsStatistics();

    @Query("""
        SELECT od.product.productId as productId,
               od.product.productName as productName,
               SUM(od.quantity) as totalQuantitySold,
               SUM(od.price * od.quantity) as totalRevenue
        FROM OrderDetails od
        GROUP BY od.product.productId, od.product.productName
        ORDER BY totalQuantitySold DESC
        LIMIT :limit
    """)
    List<TopSellingProductProjection> getTopSellingProducts(@Param("limit") int limit);

    @Query("""
        SELECT od.product.productId as productId,
               od.product.productName as productName,
               COUNT(DISTINCT od.order.orderId) as orderCount,
               SUM(od.quantity) as totalQuantity,
               SUM(od.price * od.quantity) as totalRevenue,
               AVG(CAST(od.quantity AS Double)) as averageOrderQuantity
        FROM OrderDetails od
        GROUP BY od.product.productId, od.product.productName
        ORDER BY totalRevenue DESC
    """)
    List<ProductSalesStatsProjection> getProductSalesStatistics();

    @Modifying
    Integer deleteOrderDetailsByOrderOrderId(Long orderId);

}
