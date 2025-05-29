package org.iti.ecomus.config;


import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SecurityScheme(
        name = "BearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI().info(new Info().title("E-Commerce Application")
                        .description("Backend APIs for E-Commerce app")
                        .version("v1.0.0")
                        .contact(new Contact().name("sad name").url("https://sad.com/").email("sad@gmail.com"))
                        .license(new License().name("License").url("/")))
                .externalDocs(new ExternalDocumentation().description("E-Commerce App Documentation")
                        .url("http://localhost:8080/swagger-ui/index.html"));
    }

}
