package org.iti.ecomus.specification;

import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.iti.ecomus.entity.User;
import org.iti.ecomus.enums.UserRole;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class UserSpecification {


    public static Specification<User> containsKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            if (keyword == null || keyword.trim().isEmpty()) return null;

            String likePattern = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("userName")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("job")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("phone")), likePattern)
            );
        };
    }

    // Dynamic specification builder from search parameters

    public static Specification<User> build(String keyword, Map<String, Object> searchParams) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            List<Predicate> predicates = new ArrayList<>();

            // Add keyword search
            if (keyword != null && !keyword.trim().isEmpty()) {
                Specification<User> keywordSpec = containsKeyword(keyword);
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
                        case "role":
                            try {
                                UserRole role = UserRole.valueOf(value.toString().toUpperCase());
                                predicates.add(criteriaBuilder.equal(root.get("role"), role));
                            } catch (IllegalArgumentException e) {
                                log.error(e.getMessage());
                            }
                            break;
                        case "job":
                            predicates.add(criteriaBuilder.like(
                                    criteriaBuilder.lower(root.get("job")),
                                    "%" + value.toString().toLowerCase() + "%"
                            ));
                            break;
                        case "userId":
                            try {
                                Long userId = Long.valueOf(value.toString());
                                predicates.add(criteriaBuilder.equal(root.get("userId"), userId));
                            } catch (NumberFormatException e) {
                                log.error(e.getMessage());
                            }
                            break;
                        case "email":
                            predicates.add(criteriaBuilder.like(
                                    criteriaBuilder.lower(root.get("email")),
                                    "%" + value.toString().toLowerCase() + "%"
                            ));
                            break;
                        case "userName":
                            predicates.add(criteriaBuilder.like(
                                    criteriaBuilder.lower(root.get("userName")),
                                    "%" + value.toString().toLowerCase() + "%"
                            ));
                            break;
                        case "phone":
                            predicates.add(criteriaBuilder.like(
                                    criteriaBuilder.lower(root.get("phone")),
                                    "%" + value.toString().toLowerCase() + "%"
                            ));
                            break;
                        case "creditLimitMin":
                            try {
                                BigDecimal creditLimit = new BigDecimal(value.toString());
                                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("creditLimit"), criteriaBuilder.literal(creditLimit)));
                            } catch (NumberFormatException e) {
                                log.error(e.getMessage());
                            }
                            break;
                            case "creditLimitMax":
                            try {
                                BigDecimal creditLimit = new BigDecimal(value.toString());
                                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("creditLimit"), criteriaBuilder.literal(creditLimit)));
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
