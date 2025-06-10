//package org.iti.ecomus.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//@Configuration
//public class StaticResourceConfig implements WebMvcConfigurer {
//
//    @Value("${local.image.storage.path}")
//    private String uploadDir;
//
//    @Value("${local.image.base.url}")
//    private String imageBaseUrl;
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // Map the base URL to the physical directory
//        Path uploadPath = Paths.get(uploadDir);
//        String absolutePath = uploadPath.toFile().getAbsolutePath();
//
//        registry.addResourceHandler(imageBaseUrl + "/**")
//                .addResourceLocations("file:/" + absolutePath + "/");
//    }
//}