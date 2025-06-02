package org.iti.ecomus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PagedResponse<T> {
    private List<T> content;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int pageSize;
    private boolean first;
    private boolean last;
    private boolean empty;

    // Search and sorting metadata
    private String sortField;
    private String sortDir;
    private String keyword;
    private Map<String, Object> searchParams;
    private Set<String> allowedSortFields;

    public <U> PagedResponse<U> mapContent(Function<List<T>, List<U>> mapper) {
        List<U> newContent = mapper.apply(content);

        PagedResponse<U> response = new PagedResponse<>();
        response.setContent(newContent);
        response.setCurrentPage(this.currentPage);
        response.setTotalPages(this.totalPages);
        response.setTotalElements(this.totalElements);
        response.setPageSize(this.pageSize);
        response.setFirst(this.first);
        response.setLast(this.last);
        response.setEmpty(this.empty);
        response.setSortField(this.sortField);
        response.setSortDir(this.sortDir);
        response.setKeyword(this.keyword);
        response.setSearchParams(this.searchParams);
        response.setAllowedSortFields(this.allowedSortFields);
        return response;
    }
}