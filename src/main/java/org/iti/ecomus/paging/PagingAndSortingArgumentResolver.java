package org.iti.ecomus.paging;

import org.iti.ecomus.config.AppConstants;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.*;

public class PagingAndSortingArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(PagingAndSortingParam.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer model,
                                  NativeWebRequest request, WebDataBinderFactory binderFactory) throws Exception {
        PagingAndSortingParam annotation = parameter.getParameterAnnotation(PagingAndSortingParam.class);

        // Standard pagination parameters
        String sortDir = request.getParameter("sortDir");
        String sortField = request.getParameter("sortField");
        String keyword = request.getParameter("keyword");

        // Collect all search parameters
        Map<String, Object> searchParams = new HashMap<>();

        // Get all parameter names and values
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String paramName = entry.getKey();
            String[] values = entry.getValue();

            // Skip pagination/sorting specific parameters
            if (!paramName.equals("sortDir") && !paramName.equals("sortField") &&
                    !paramName.equals("keyword") && !paramName.equals("pageNum") &&
                    !paramName.equals("pageSize")) {

                if (values != null && values.length > 0) {
                    if (values.length == 1) {
                        searchParams.put(paramName, values[0]);
                    } else {
                        searchParams.put(paramName, values);
                    }
                }
            }
        }

        String reverseSortDir = sortDir != null ? (sortDir.equals("asc") ? "desc" : "asc") : "desc";

        // Add attributes to model
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchParams", searchParams);
        model.addAttribute("moduleURL", annotation.model());

        // Create allowed sort fields set
        Set<String> allowedSortFields = new HashSet<>();

        switch (annotation.model()){
            case AppConstants.USER_MODEL:
                allowedSortFields.addAll(Arrays.asList(AppConstants.ALLLOWED_USER_SEARCH_FIELDS));
                break;
            case AppConstants.PRODUCT_MODEL:
                allowedSortFields.addAll(Arrays.asList(AppConstants.ALLLOWED_PRODUCT_SEARCH_FIELDS));
                break;
            case AppConstants.ORDER_MODEL:
                allowedSortFields.addAll(Arrays.asList(AppConstants.ALLLOWED_ORDER_SEARCH_FIELDS));
                break;
            case AppConstants.CART_MODEL:
                allowedSortFields.addAll(Arrays.asList(AppConstants.ALLLOWED_CART_SEARCH_FIELDS));
                break;
            case AppConstants.ADDRESS_MODEL:
                allowedSortFields.addAll(Arrays.asList(AppConstants.ALLLOWED_ADDRESS_SEARCH_FIELDS));
                break;
            default:
                break;
        }
//        System.out.println(searchParams);
        return new PagingAndSortingHelper(
                sortField, sortDir, keyword, searchParams, allowedSortFields, annotation.defaultSortField(),annotation.isUser());
    }
}
