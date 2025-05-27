package org.iti.ecomus.config;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;

//@Testcontainers
//@TestConfiguration
public class TestContainersConfig {

    @Bean
    @ServiceConnection
    MySQLContainer<?> mysqlContainer() {
        return new MySQLContainer<>("mysql:8.0.32")
                .withDatabaseName("iti_grad")
                .withUsername("root")
                .withPassword("root");

    }
}
