package org.iti.ecomus.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.iti.ecomus.entity.Category;
import org.iti.ecomus.entity.Order;
import org.iti.ecomus.entity.Product;
import org.iti.ecomus.entity.User;
import org.iti.ecomus.enums.OrderStatus;
import org.iti.ecomus.enums.PayType;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class OrderSpecification {

    public static Specification<Order> containsKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.trim().isEmpty()) return null;

            Join<Product, Category> categoryJoin = root.join("categories");

            String likePattern = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("price")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("status")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("payType")), likePattern),

                    criteriaBuilder.like(criteriaBuilder.lower(categoryJoin.get("categoryName")), likePattern)
            );
        };
    }

    public static Specification<Order> build(String keyword, Map<String, Object> searchParams) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Add keyword search
            if (keyword != null && !keyword.trim().isEmpty()) {
                Specification<Order> keywordSpec = containsKeyword(keyword);
                Predicate keywordPredicate = keywordSpec.toPredicate(root, query, criteriaBuilder);
                if (keywordPredicate != null) {
                    predicates.add(keywordPredicate);
                }
            }

            // Add search parameters
            if (searchParams != null) {
                for (Map.Entry<String, Object> entry : searchParams.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();

                    if (value == null || (value instanceof String && ((String) value).trim().isEmpty())) {
                        continue;
                    }

                    switch (key) {
                        case "payType":
                            try {
                                PayType payType = PayType.valueOf(value.toString().toUpperCase());
                                predicates.add(criteriaBuilder.equal(
                                       root.get("payType"),payType));
                            } catch (IllegalArgumentException e) {
                                log.error(e.getMessage());
                            }
                            break;
                        case "status":
                            try {
                                OrderStatus status = OrderStatus.valueOf(value.toString().toUpperCase());
                                predicates.add(criteriaBuilder.equal(
                                        root.get("status"),status));
                            } catch (IllegalArgumentException e) {
                                log.error(e.getMessage());
                            }
                            break;
                        case "priceMin":
                            try {
                                BigDecimal v = BigDecimal.valueOf(Long.parseLong((String) value));
                                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), v));
                            }catch (Exception e) {
                            log.error("Invalid value for priceMin: " + value);
                        }
                            break;
                        case "priceMax":
                            try {
                                BigDecimal v = BigDecimal.valueOf(Long.parseLong((String) value));
                                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), v));
                            }catch (Exception e) {
                                log.error("Invalid value for priceMax: " + value);
                            }
                            break;
                        case "orderIdMax":
                            try {
                                Integer v = Integer.valueOf(value.toString());
                                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("orderId"), v));
                            }catch (Exception e) {
                                log.error("Invalid value for orderIdMax: " + value);
                            }
                            break;
                        case "orderIdMin":
                            try {
                                Integer v = Integer.valueOf(value.toString());
                                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("orderId"), v));
                            }catch (Exception e) {
                            log.error("Invalid value for orderIdMin: " + value);
                        }
                            break;
                        case "address":
                            try {
                                predicates.add(criteriaBuilder.like(
                                        criteriaBuilder.lower(root.get("address")),
                                        "%" + value.toString().toLowerCase() + "%"));
                            } catch (IllegalArgumentException e) {
                                log.error(e.getMessage());
                            }
                            break;
                        case "userId":
                            try {
                                Long userId = Long.valueOf(value.toString());
                                Join<Order, User> userJoin = root.join("user");
                                predicates.add(criteriaBuilder.equal(userJoin.get("userId"), userId));

                            } catch (NumberFormatException e) {
                                log.error(e.getMessage());
                            }
                            break;
                        default:
                            // Unknown parameter, skip or log
                            log.info("Unknown search parameter: " + key);
                            break;
                    }
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
