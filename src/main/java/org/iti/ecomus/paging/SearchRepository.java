package org.iti.ecomus.paging;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Map;

@NoRepositoryBean
public interface SearchRepository<T, ID> extends PagingAndSortingRepository<T, ID> , JpaSpecificationExecutor<T> {
//    Page<T> findAll(String keyword, Pageable pageable);

//    Page<T> findAllWithFilters(String keyword, Map<String, Object> searchParams, Pageable pageable);




    default Page<T> searchWithFilters(String keyword, Map<String, Object> searchParams, Pageable pageable) {
        return findAll(getFiltersSpecification(keyword, searchParams), pageable);
    }


    Specification<T> getFiltersSpecification(String keyword, Map<String, Object> searchParams);


}