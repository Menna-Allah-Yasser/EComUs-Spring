package org.iti.ecomus.config;

import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.iti.ecomus.paging.PagingAndSortingArgumentResolver;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

@Configuration
public class WebConfig implements WebMvcConfigurer {

        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
            resolvers.add(new PagingAndSortingArgumentResolver());
        }
    @Bean
    public CacheManager ehCacheManager() {
        CachingProvider provider = Caching.getCachingProvider();
        CacheManager cacheManager = provider.getCacheManager(
                provider.getDefaultURI(),
                provider.getDefaultClassLoader(),
                provider.getDefaultProperties()
        );

//        CacheConfigurationBuilder<String, String> configuration =
//                CacheConfigurationBuilder.newCacheConfigurationBuilder(
//                                String.class,
//                                String.class,
//                                ResourcePoolsBuilder
//                                        .newResourcePoolsBuilder().offheap(1, MemoryUnit.MB))
//                        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(20)));
//
//        javax.cache.configuration.Configuration<String, String> stringDoubleConfiguration =
//                Eh107Configuration.fromEhcacheCacheConfiguration(configuration);
//
//        cacheManager.createCache("users", stringDoubleConfiguration);
        return cacheManager;

    }
//    @Bean
//    public CacheManager springCacheManager(CacheManager cm) {
//        return new JCacheCacheManager(cm);
//    }
//    @Bean
//    public JavaMailSender getJavaMailSender() {
//        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
//        mailSender.setHost("smtp.gmail.com");
//        mailSender.setPort(587);
//
//        mailSender.setUsername("ecomsender31@gmail.com");
//        mailSender.setPassword("3VcYn2X1v1PF");
//
//        Properties props = mailSender.getJavaMailProperties();
//        props.put("mail.transport.protocol", "smtp");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.debug", "true");
//
//        return mailSender;
//    }
}
