package org.iti.ecomus.repository;

import com.sun.jdi.InterfaceType;
import org.iti.ecomus.dto.stats.*;
import org.iti.ecomus.entity.User;
import org.iti.ecomus.paging.SearchRepository;
import org.iti.ecomus.specification.UserSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Repository
public interface UserRepo extends JpaRepository<User, Long>, SearchRepository<User,Long> {


    @Query("SELECT COUNT(u) FROM User u WHERE u.email = :email")
    Long checkValidEmail(@Param("email") String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByUserName(String userName);

    boolean existsByEmail(String email);


    //statistics queries


    @Query("""
        SELECT u.role as role,
               COUNT(u) as count
        FROM User u
        GROUP BY u.role
    """)
    List<UserRoleDistributionProjection> getUserRoleDistribution();

    @Query("""
        SELECT u.userId as userId,
               u.userName as userName,
               u.email as email,
               COUNT(o) as totalOrders,
               COALESCE(SUM(o.price), 0) as totalSpent,
               COALESCE(AVG(o.price), 0) as averageOrderValue,
               MAX(o.date) as lastOrderDate
        FROM User u
        LEFT JOIN u.orders o
        GROUP BY u.userId, u.userName, u.email
        HAVING COUNT(o) > 0
        ORDER BY totalSpent DESC
    """)
    List<CustomerOrderStatsProjection> getCustomerOrderStatistics();

    @Query("""
        SELECT u.userId as userId,
               u.userName as userName,
               u.email as email,
               COALESCE(SUM(o.price), 0) as totalSpent,
               COUNT(o) as totalOrders
        FROM User u
        LEFT JOIN u.orders o
        GROUP BY u.userId, u.userName, u.email
        HAVING COUNT(o) > 0
        ORDER BY totalSpent DESC
        LIMIT :limit
    """)
    List<TopCustomersProjection> getTopCustomers(@Param("limit") int limit);

    @Override
    default public Specification<User> getFiltersSpecification(String keyword, Map<String, Object> searchParams) {
        return UserSpecification.build(keyword, searchParams);
    }
}
