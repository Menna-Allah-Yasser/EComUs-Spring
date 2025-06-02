package org.iti.ecomus.config;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.parameters.Parameter;

import org.iti.ecomus.paging.PagingAndSortingParam;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;
import java.util.Iterator;
@Component
    @Order(1) // Run before the main customizer
    public class HidingPagingHelperSwagger implements OperationCustomizer {

        @Override
        public Operation customize(Operation operation, HandlerMethod handlerMethod) {
            Method method = handlerMethod.getMethod();

            // Check if any parameter has @PagingAndSortingParam annotation
            java.lang.reflect.Parameter[] parameters = method.getParameters();
            for (java.lang.reflect.Parameter param : parameters) {
                PagingAndSortingParam annotation = param.getAnnotation(PagingAndSortingParam.class);
                if (annotation != null) {
                    // Remove any automatically generated parameter for PagingAndSortingHelper
                    if (operation.getParameters() != null) {
                        Iterator<Parameter> iterator = operation.getParameters().iterator();
                        while (iterator.hasNext()) {
                            Parameter swaggerParam = iterator.next();
                            // Remove parameters that might be auto-generated for the helper object
                            if (swaggerParam.getName() != null &&
                                    (swaggerParam.getName().contains("helper") ||
                                            swaggerParam.getName().contains("PagingAndSortingHelper"))) {
                                iterator.remove();
                            }
                        }
                    }
                    break;
                }
            }

            return operation;
        }
    }