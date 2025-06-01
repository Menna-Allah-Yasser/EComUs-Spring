package org.iti.ecomus.paging;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.iti.ecomus.dto.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;

@Getter
@Setter
@Slf4j
public class PagingAndSortingHelper {
    private String sortField;
    private String sortDir;
    private String keyword;
    private Map<String, Object> searchParams;
    private Set<String> allowedSortFields;
    private String defaultSortField;
    private boolean isUser;

    public PagingAndSortingHelper(String sortField, String sortDir, String keyword,
                                  Map<String, Object> searchParams, Set<String> allowedSortFields, String defaultSortField,boolean isUser) {
        this.sortField = validateSortField(sortField, allowedSortFields, defaultSortField);
        this.sortDir = validateSortDir(sortDir);
        this.keyword = keyword;
        this.searchParams = searchParams;
        this.allowedSortFields = allowedSortFields;
        this.defaultSortField = defaultSortField;
        this.isUser = isUser;
    }

    public <T> PagedResponse<T> getPagedResponse(int pageNum, int pageSize, SearchRepository<T, ?> repo,Long userId) {
        Pageable pageable = createPageable(pageSize, pageNum);
        Page<T> page = null;


        if(isUser) {

            if (searchParams == null) {
                searchParams = new HashMap<>();
            }
            searchParams.put("userId", userId);
        }

        try {

            if ((keyword != null && !keyword.trim().isEmpty()) ||
                    (searchParams != null && !searchParams.isEmpty())) {
//                System.out.println(keyword + " " + searchParams);
                page = repo.searchWithFilters(keyword, searchParams, pageable);
            } else {
                page = repo.findAll(pageable);
            }
        } catch (Exception e) {
            // If sorting fails, fallback to default sorting
            log.info("Sorting failed, using default: " + e.getMessage());
            pageable = PageRequest.of(pageNum - 1, pageSize,
                    Sort.by(Sort.Direction.ASC, defaultSortField));

            // Retry with default sorting
            if ((keyword != null && !keyword.trim().isEmpty()) ||
                    (searchParams != null && !searchParams.isEmpty())) {
                page = repo.searchWithFilters(keyword, searchParams, pageable);
            } else {
                page = repo.findAll(pageable);
            }
        }

        return createPagedResponse(page);
    }

    private <T> PagedResponse<T> createPagedResponse(Page<T> page) {
        PagedResponse<T> response = new PagedResponse<>();
        response.setContent(page.getContent());
        response.setCurrentPage(page.getNumber() + 1); // Spring uses 0-based, frontend uses 1-based
        response.setTotalPages(page.getTotalPages());
        response.setTotalElements(page.getTotalElements());
        response.setPageSize(page.getSize());
        response.setFirst(page.isFirst());
        response.setLast(page.isLast());
        response.setEmpty(page.isEmpty());

        // Add sorting and search info
        response.setSortField(this.sortField);
        response.setSortDir(this.sortDir);
        response.setKeyword(this.keyword);
        response.setSearchParams(this.searchParams);
        response.setAllowedSortFields(this.allowedSortFields);

        return response;
    }

    private String validateSortField(String sortField, Set<String> allowedFields, String defaultField) {
        if (sortField == null || sortField.trim().isEmpty()) {
            return defaultField;
        }

        // Check if the sort field is allowed
        if (allowedFields != null && !allowedFields.contains(sortField)) {
            // Log the invalid attempt if needed
            log.info("Invalid sort field attempted: " + sortField + ". Using default: " + defaultField);
            return defaultField;
        }

        return sortField;
    }

    private String validateSortDir(String sortDir) {
        if (sortDir == null || (!sortDir.equals("asc") && !sortDir.equals("desc"))) {
            return "asc"; // default
        }
        return sortDir;
    }

    public Pageable createPageable(int pageSize, int pageNum) {
        if (sortField != null && !sortField.trim().isEmpty()) {
            Sort.Direction direction = sortDir.equals("desc") ?
                    Sort.Direction.DESC : Sort.Direction.ASC;
            Sort sort = Sort.by(direction, sortField);
            return PageRequest.of(pageNum - 1, pageSize, sort);
        }
        return PageRequest.of(pageNum - 1, pageSize);
    }

    // Static helper methods for creating allowed fields
    public static Set<String> createAllowedFields(String... fields) {
        return new HashSet<>(Arrays.asList(fields));
    }
}