package org.iti.ecomus.paging;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface PagingAndSortingParam {
    String model();
    boolean isUser() default false;
    String defaultSortField() default "id";
}