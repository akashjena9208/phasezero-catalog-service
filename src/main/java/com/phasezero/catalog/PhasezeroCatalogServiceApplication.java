package com.phasezero.catalog;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@OpenAPIDefinition(
		info = @Info(
				title = "PhaseZero Product Catalog API",
				version = "1.0",
				description = "Backend microservice for managing product catalog data"
		)
)
@SpringBootApplication
@EnableCaching
public class PhasezeroCatalogServiceApplication {

	public static void main(String[] args) {

		SpringApplication.run(PhasezeroCatalogServiceApplication.class, args);
	}

}
