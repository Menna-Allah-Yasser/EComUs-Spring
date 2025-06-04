package org.iti.ecomus.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.iti.ecomus.entity.Wishlist;
import org.iti.ecomus.entity.Category;
import org.iti.ecomus.entity.Product;
import org.iti.ecomus.entity.User;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class WishlistSpecification {

    public static Specification<Wishlist> containsKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.trim().isEmpty()) return null;


            Join<Wishlist, Product> productJoin = root.join("product");
            Join<Product, Category> categoryJoin = productJoin.join("categories");


            String likePattern = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(productJoin.get("productName")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(productJoin.get("description")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(categoryJoin.get("categoryName")), likePattern)
            );
        };
    }

    public static Specification<Wishlist> build(String keyword, Map<String, Object> searchParams) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Add keyword search
            if (keyword != null && !keyword.trim().isEmpty()) {
                Specification<Wishlist> keywordSpec = containsKeyword(keyword);
                Predicate keywordPredicate = keywordSpec.toPredicate(root, query, criteriaBuilder);
                if (keywordPredicate != null) {
                    predicates.add(keywordPredicate);
                }
            }

            // Add search parameters
            if (searchParams != null) {
                Join<Wishlist,Product> productJoin = root.join("product");
                Join<Product, Category> categoryJoin = productJoin.join("categories");

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
                                        criteriaBuilder.lower(productJoin.get("productName")),"%" + value.toString().toLowerCase() + "%"));
                            } catch (IllegalArgumentException e) {
                                log.error(e.getMessage());
                            }
                            break;
                        case "description":
                            try {
                                predicates.add(criteriaBuilder.like(
                                        criteriaBuilder.lower(productJoin.get("description")),"%" + value.toString().toLowerCase() + "%"));
                            } catch (IllegalArgumentException e) {
                                log.error(e.getMessage());
                            }
                            break;
                        case "priceMin":
                            try {

                                BigDecimal v = BigDecimal.valueOf(Long.parseLong((String) value));
                                predicates.add(criteriaBuilder.greaterThanOrEqualTo(productJoin.get("price"), v));
                            }catch (Exception e) {
                                log.error("Invalid value for priceMin: " + value);
                            }

                            break;
                        case "priceMax":
                            try {
                                BigDecimal v = BigDecimal.valueOf(Long.parseLong((String) value));
                                predicates.add(criteriaBuilder.lessThanOrEqualTo(productJoin.get("price"), v));
                            }catch (Exception e) {
                                log.error("Invalid value for priceMin: " + value);
                            }
                            break;
                        case "categoryId":
                            try {
                                Long categoryId = Long.valueOf(value.toString());

                                predicates.add(criteriaBuilder.equal(categoryJoin.get("categoryId"), categoryId));
                            } catch (NumberFormatException e) {
                                log.error(e.getMessage());
                            }
                            break;
                        case "categoryName":
                            try {

                                predicates.add(criteriaBuilder.like(
                                        criteriaBuilder.lower(categoryJoin.get("categoryName")),
                                        "%" + value.toString().toLowerCase() + "%"));
                            } catch (IllegalArgumentException e) {
                                log.error(e.getMessage());
                            }
                            break;
                        case "userId":
                            try {
                                Long userId = Long.valueOf(value.toString());
                                Join<Wishlist, User> userJoin = root.join("user");

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
