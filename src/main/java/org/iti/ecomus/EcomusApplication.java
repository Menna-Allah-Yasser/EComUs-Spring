package org.iti.ecomus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@EnableJpaRepositories(basePackages = "org.iti.ecomus.repository")
public class EcomusApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcomusApplication.class, args);
	}

}
