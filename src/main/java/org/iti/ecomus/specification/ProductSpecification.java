package org.iti.ecomus.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.iti.ecomus.entity.Category;
import org.iti.ecomus.entity.Product;
import org.iti.ecomus.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ProductSpecification {

    public static Specification<Product> containsKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.trim().isEmpty()) return null;

            Join<Product, Category> categoryJoin = root.join("categories");

            String likePattern = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("productName")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(categoryJoin.get("categoryName")), likePattern)
            );
        };
    }
    public static Specification<Product> build(String keyword, Map<String, Object> searchParams) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Add keyword search
            if (keyword != null && !keyword.trim().isEmpty()) {
                Specification<Product> keywordSpec = containsKeyword(keyword);
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
                        case "productName":
                            try {
                                predicates.add(criteriaBuilder.like(
                                        criteriaBuilder.lower(root.get("productName")),"%" + value.toString().toLowerCase() + "%"));
                            } catch (IllegalArgumentException e) {
                                log.error(e.getMessage());
                            }
                            break;
                        case "description":
                            try {
                                predicates.add(criteriaBuilder.like(
                                        criteriaBuilder.lower(root.get("description")),"%" + value.toString().toLowerCase() + "%"));
                            } catch (IllegalArgumentException e) {
                                log.error(e.getMessage());
                            }
                            break;
                        case "priceMin":
                            if (value instanceof BigDecimal v) {
                                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), v));
                            } else {
                                log.error("Invalid value for priceMin: " + value);
                            }
                            break;
                        case "priceMax":
                            if (value instanceof BigDecimal v) {
                                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), v));
                            } else {
                                log.error("Invalid value for priceMax: " + value);
                            }
                            break;
                        case "categoryId":
                            try {
                                Long categoryId = Long.valueOf(value.toString());
                                Join<Product, Category> categoryJoin = root.join("categories");
                                predicates.add(criteriaBuilder.equal(categoryJoin.get("categoryId"), categoryId));
                            } catch (NumberFormatException e) {
                                log.error(e.getMessage());
                            }
                            break;
                        case "quantityMin":
                            if (value instanceof Integer v) {
                                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("quantity"), v));
                            } else {
                                log.error("Invalid value for quantityMin: " + value);
                            }
                            break;
                        case "quantityMax":
                            if (value instanceof Integer v) {
                                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("quantity"), v));
                            } else {
                                log.error("Invalid value for quantityMax: " + value);
                            }
                            break;
                        case "categoryName":
                            try {
                                Join<Product, Category> categoryJoin = root.join("categories");
                                predicates.add(criteriaBuilder.like(
                                        criteriaBuilder.lower(categoryJoin.get("categoryName")),
                                        "%" + value.toString().toLowerCase() + "%"));
                            } catch (IllegalArgumentException e) {
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
