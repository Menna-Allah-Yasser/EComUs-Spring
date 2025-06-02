package org.iti.ecomus.config;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.iti.ecomus.enums.OrderStatus;
import org.iti.ecomus.enums.PayType;
import org.iti.ecomus.enums.UserRole;
import org.iti.ecomus.paging.PagingAndSortingParam;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class PagingAndSortingSwaggerCustomizer implements OperationCustomizer {

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        Method method = handlerMethod.getMethod();

        // Check if any parameter has @PagingAndSortingParam annotation
        java.lang.reflect.Parameter[] parameters = method.getParameters();
        for (java.lang.reflect.Parameter param : parameters) {
            PagingAndSortingParam annotation = param.getAnnotation(PagingAndSortingParam.class);
            if (annotation != null) {
                addPagingAndSortingParameters(operation, annotation);
                break;
            }
        }

        return operation;
    }

    private void addPagingAndSortingParameters(Operation operation, PagingAndSortingParam annotation) {
        String model = annotation.model();
        String defaultSortField = annotation.defaultSortField();

        // Get allowed sort fields based on model
        Set<String> allowedSortFields = getAllowedSortFields(model);
        Set<String> allowedSearchFields = getAllowedSearchFields(model);

        // Add standard pagination parameters
//        operation.addParametersItem(createParameter("pageNum", "integer", "Page number (1-based)", "1", false));
//        operation.addParametersItem(createParameter("pageSize", "integer", "Number of items per page", "10", false));

        // Add sorting parameters
        Parameter sortFieldParam = createParameter("sortField", "string",
                "Field to sort by. Allowed values: " + String.join(", ", allowedSortFields),
                defaultSortField, false);
        sortFieldParam.getSchema().setEnum(allowedSortFields.stream().toList());
        operation.addParametersItem(sortFieldParam);

        Parameter sortDirParam = createParameter("sortDir", "string",
                "Sort direction", "asc", false);
        sortDirParam.getSchema().setEnum(Arrays.asList("asc", "desc"));
        operation.addParametersItem(sortDirParam);

        // Add keyword search parameter
        operation.addParametersItem(createParameter("keyword", "string",
                "Keyword to search across multiple fields", null, false));

        // Add specific search parameters based on model
        addModelSpecificSearchParameters(operation, model, allowedSearchFields);
    }

    private void addModelSpecificSearchParameters(Operation operation, String model, Set<String> allowedFields) {
        switch (model) {
            case AppConstants.USER_MODEL:
                addUserSearchParameters(operation);
                break;
            case AppConstants.PRODUCT_MODEL:
                addProductSearchParameters(operation);
                break;
            case AppConstants.ORDER_MODEL:
                addOrderSearchParameters(operation);
                break;
            case AppConstants.CART_MODEL:
                addCartSearchParameters(operation);
                break;
        }
    }

    private void addUserSearchParameters(Operation operation) {
        operation.addParametersItem(createParameter("userId", "integer", "Filter by user ID", null, false));
        operation.addParametersItem(createParameter("userName", "string", "Filter by username", null, false));
        operation.addParametersItem(createParameter("email", "string", "Filter by email", null, false));

        Parameter roleParam = createParameter("role", "string", "Filter by user role", null, false);
        roleParam.getSchema().setEnum(Arrays.asList(UserRole.USER, UserRole.ADMIN));
        operation.addParametersItem(roleParam);

        operation.addParametersItem(createParameter("job", "string", "Filter by job", null, false));
        operation.addParametersItem(createParameter("phone", "string", "Filter by phone number", null, false));
        operation.addParametersItem(createParameter("creditLimit", "number", "Filter by credit limit", null, false));
    }

    private void addProductSearchParameters(Operation operation) {
//        operation.addParametersItem(createParameter("productId", "integer", "Filter by product ID", null, false));
        operation.addParametersItem(createParameter("productName", "string", "Filter by product name", null, false));
        operation.addParametersItem(createParameter("description", "string", "Filter by description", null, false));
        operation.addParametersItem(createParameter("priceMin", "number", "Filter by minimum price", null, false));
        operation.addParametersItem(createParameter("priceMax", "number", "Filter by maximum price", null, false));
        operation.addParametersItem(createParameter("quantityMin", "integer", "Filter by minimum quantity", null, false));
        operation.addParametersItem(createParameter("quantityMax", "integer", "Filter by maximum quantity", null, false));
        operation.addParametersItem(createParameter("categoryId", "string", "Filter by categoryId", null, false));
        operation.addParametersItem(createParameter("categoryName", "string", "Filter by categoryName", null, false));

    }

    private void addOrderSearchParameters(Operation operation) {
//        operation.addParametersItem(createParameter("orderId", "integer", "Filter by order ID", null, false));
        operation.addParametersItem(createParameter("priceMin", "integer", "Filter by priceMin", null, false));
        operation.addParametersItem(createParameter("priceMax", "integer", "Filter by priceMax", null, false));
        operation.addParametersItem(createParameter("orderIdMax", "integer", "Filter by order ID MAX", null, false));
        operation.addParametersItem(createParameter("orderIdMin", "integer", "Filter by order ID MIN", null, false));
        operation.addParametersItem(createParameter("address", "string", "Filter by address", null, false));
        Parameter status = createParameter("OrderStatus", "string", "Filter by OrderStatus", null, false);
        status.getSchema().setEnum(Arrays.asList(OrderStatus.CANCELED,
                OrderStatus.COMPLETED, OrderStatus.PROCESSING, OrderStatus.SHIPPED));
        operation.addParametersItem(status);
        Parameter payType = createParameter("payType", "string", "Filter by payment type", null, false);
        payType.getSchema().setEnum(Arrays.asList(PayType.CASH,PayType.CREDIT));
        operation.addParametersItem(payType);
        operation.addParametersItem(createParameter("userId", "string", "Filter by userId", null, false));
    }

    private void addCartSearchParameters(Operation operation) {
        operation.addParametersItem(createParameter("productName", "string", "Filter by product name", null, false));
        operation.addParametersItem(createParameter("description", "string", "Filter by description", null, false));
        operation.addParametersItem(createParameter("priceMin", "number", "Filter by minimum price", null, false));
        operation.addParametersItem(createParameter("priceMax", "number", "Filter by maximum price", null, false));
        operation.addParametersItem(createParameter("quantityMin", "integer", "Filter by minimum quantity", null, false));
        operation.addParametersItem(createParameter("quantityMax", "integer", "Filter by maximum quantity", null, false));
        operation.addParametersItem(createParameter("categoryId", "string", "Filter by categoryId", null, false));
        operation.addParametersItem(createParameter("categoryName", "string", "Filter by categoryName", null, false));

    }


    private Parameter createParameter(String name, String type, String description, String defaultValue, boolean required) {
        Parameter parameter = new Parameter();
        parameter.setName(name);
        parameter.setIn("query");
        parameter.setDescription(description);
        parameter.setRequired(required);

        Schema<?> schema = new Schema<>();
        schema.setType(type);
        if (defaultValue != null) {
            schema.setDefault(defaultValue);
        }
        parameter.setSchema(schema);

        return parameter;
    }

    private Set<String> getAllowedSortFields(String model) {
        switch (model) {
            case AppConstants.USER_MODEL:
                return new HashSet<>(Arrays.asList(AppConstants.ALLLOWED_USER_SEARCH_FIELDS));
            case AppConstants.PRODUCT_MODEL:
                return new HashSet<>(Arrays.asList(AppConstants.ALLLOWED_PRODUCT_SEARCH_FIELDS));
            case AppConstants.ORDER_MODEL:
                return new HashSet<>(Arrays.asList(AppConstants.ALLLOWED_ORDER_SEARCH_FIELDS));
            case AppConstants.CART_MODEL:
                return new HashSet<>(Arrays.asList(AppConstants.ALLLOWED_CART_SEARCH_FIELDS));
            case AppConstants.ADDRESS_MODEL:
                return new HashSet<>(Arrays.asList(AppConstants.ALLLOWED_ADDRESS_SEARCH_FIELDS));
            default:
                return new HashSet<>();
        }
    }

    private Set<String> getAllowedSearchFields(String model) {
        switch (model) {
            case AppConstants.USER_MODEL:
                return new HashSet<>(Arrays.asList(
                        "userId", "userName", "email", "role", "job", "phone", "creditLimit"
                ));
            case AppConstants.PRODUCT_MODEL:
                return new HashSet<>(Arrays.asList(
                        "productName", "description", "priceMin", "priceMax",
                        "quantityMin", "quantityMax", "categoryId", "categoryName"
                ));
            case AppConstants.ORDER_MODEL:
                return new HashSet<>(Arrays.asList(
                        "priceMin", "priceMax", "orderIdMax", "orderIdMin",
                        "address", "OrderStatus", "payType", "userId"
                ));
            case AppConstants.CART_MODEL:
                return new HashSet<>(Arrays.asList(
                        "productName", "description", "priceMin", "priceMax",
                        "quantityMin", "quantityMax", "categoryId", "categoryName"
                ));
            case AppConstants.ADDRESS_MODEL:
                return new HashSet<>(Arrays.asList(
                        "id", "user", "street", "city", "area", "buildingNo"
                ));
            default:
                return new HashSet<>();
        }
    }
}