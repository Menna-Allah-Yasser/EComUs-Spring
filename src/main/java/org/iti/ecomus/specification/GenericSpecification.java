package org.iti.ecomus.specification;

import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

public interface GenericSpecification<T> {
    Specification<T> build(String keyword, Map<String, Object> searchParams);

    Specification<T> containsKeyword(String keyword);
}
