package org.iti.ecomus.dto;

import lombok.Data;

import java.util.Map;

@Data
public class SearchRequest {
    private String keyword;
    private Map<String, Object> filters;
    private String sortField;
    private String sortDir;
    private int page = 1;
    private int size = 10;
}