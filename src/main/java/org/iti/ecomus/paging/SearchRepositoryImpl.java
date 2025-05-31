package org.iti.ecomus.paging;

import jakarta.persistence.EntityManager;
import org.iti.ecomus.specification.GenericSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.io.Serializable;
import java.util.Map;

//public class SearchRepositoryImpl<T, ID extends Serializable>
//        extends SimpleJpaRepository<T, ID>
//        implements SearchRepository<T, ID> {
//
//    private final EntityManager entityManager;
//    private final GenericSpecification<T> specBuilder;
//
//    public SearchRepositoryImpl(JpaEntityInformation<T, ?> entityInformation,
//                                EntityManager entityManager,
//                                GenericSpecification<T> specBuilder) {
//        super(entityInformation, entityManager);
//        this.entityManager = entityManager;
//        this.specBuilder = specBuilder;
//    }
//
//
//    @Override
//    public Specification<T> getKeywordSpecification(String keyword) {
//        return specBuilder.containsKeyword(keyword);
//    }
//
//    @Override
//    public Specification<T> getFiltersSpecification(String keyword, Map<String, Object> searchParams) {
//        return specBuilder.build(keyword, searchParams);
//    }
//}