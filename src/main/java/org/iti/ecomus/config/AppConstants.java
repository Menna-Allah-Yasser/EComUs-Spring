package org.iti.ecomus.config;

public class AppConstants {
    public static final String PAGE_NUMBER = "1";
    public static final String PAGE_SIZE = "10";
    public static final String SORT_CATEGORIES_BY = "categoryId";
    public static final String SORT_PRODUCTS_BY = "productId";
    public static final String SORT_USERS_BY = "userId";
    public static final String SORT_ORDERS_BY = "totalAmount";
    public static final String SORT_DIR = "asc";
    public static final Long ADMIN_ID = 101L;
    public static final Long USER_ID = 102L;
    public static final int JWT_TOKEN_VALIDITY = 5 * 60 * 60;
    public static final int JWT_Refresh_TOKEN_VALIDITY = 24 *30 * 60 * 60;
    public static final String[] PUBLIC_URLS = { "/v3/api-docs/**", "/swagger-ui/**", "/api/auth/**","/static/**"
            ,"/css/**","/js/**","/images/**","/","/index.html","/api/public/products/**","/api/public/category/**" };
    public static final String[] USER_URLS = { "/api/public/**" };
    public static final String[] ADMIN_URLS = { "/api/admin/**" };


    //Models for Search
    public static final String USER_MODEL = "users";
    public static final String ORDER_MODEL = "orders";
    public static final String PRODUCT_MODEL = "products";
    public static final String CART_MODEL = "carts";
    public static final String ADDRESS_MODEL = "addresses";
    public static final String WISH_MODEL = "wishes";

    //Allowed Fields for Search
    public static final String[] ALLLOWED_USER_SEARCH_FIELDS = {"userId", "userName", "email", "role", "job", "phone", "BD", "creditLimit"};
    public static final String[] ALLLOWED_ORDER_SEARCH_FIELDS = {"orderId", "address", "status", "price", "payType","user"};
    public static final String[] ALLLOWED_PRODUCT_SEARCH_FIELDS = {"productId", "productName", "description", "price","quantity","purchaseCount","categories"};
    public static final String[] ALLLOWED_ADDRESS_SEARCH_FIELDS = {"id", "user", "street", "city", "area", "buildingNo"};
    public static final String[] ALLLOWED_CART_SEARCH_FIELDS = { "userId", "productId","quantity"};
    public static final String[] ALLLOWED_WISH_SEARCH_FIELDS = { "userId", "productId"};

}
