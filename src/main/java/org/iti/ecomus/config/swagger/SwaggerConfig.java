package org.iti.ecomus.config.swagger;


import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@SecurityScheme(
        name = "BearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@Configuration
public class SwaggerConfig {

//    @Bean
//    public GroupedOpenApi adminApi() {
//        return GroupedOpenApi.builder()
//                .group("admin-api")
//                .packagesToScan("org.iti.ecomus.controller.admin")
//                .pathsToMatch("/api/admin/**")
//                .build();
//    }
//
//
//    @Bean
//    public GroupedOpenApi publicApi() {
//        return GroupedOpenApi.builder()
//                .group("public-api")
//                .packagesToScan("org.iti.ecomus.controller.customer")
//                .pathsToMatch("/api/public/**")
//                .build();
//    }
//
//    @Bean
//    public GroupedOpenApi authApi() {
//        return GroupedOpenApi.builder()
//                .group("auth-api")
////                .packagesToScan("org.iti.ecomus.config.security")
//                .pathsToMatch("/api/auth/**")
//                .build();
//    }



    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI().info(new Info().title("E-Commerce Application")
                        .description("Backend APIs for E-Commerce app")
                        .version("v1.0.0")
                        .contact(new Contact().name("sad name").url("https://sad.com/").email("sad@gmail.com"))
                        .license(new License().name("License").url("/")))
                .externalDocs(new ExternalDocumentation().description("E-Commerce App Documentation")
                        .url("http://localhost:8080/swagger-ui/index.html"))
        .tags(Arrays.asList(
                                new Tag().name("Authentication").description("User authentication and registration"),
                                new Tag().name("Admin").description("Administrative operations"),
                                new Tag().name("Admin - Users").description("Admin user management"),
                                new Tag().name("Admin - Products").description("Admin product management"),
                                new Tag().name("Admin - Orders").description("Admin order management"),
                                new Tag().name("Admin - Categories").description("Admin category management"),
                                new Tag().name("Customer").description("Customer operations"),
                                new Tag().name("Customer - Products").description("Customer product browsing"),
                                new Tag().name("Customer - Orders").description("Customer order management"),
                                new Tag().name("Customer - Profile").description("Customer profile management"),
                        new Tag().name("Customer - Cart").description("Shopping cart operations"),
                        new Tag().name("Customer - Addresses").description("Customer address management"),
                new Tag().name("Customer - Wishlist").description( "Customer wishlist"),
                new Tag().name("Customer - Categories").description("Customer category browsing")
                        ));
    }

}
