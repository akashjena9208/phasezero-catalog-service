package com.phasezero.catalog.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI phaseZeroOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("PhaseZero Product Catalog API")
                        .description("Backend microservice for managing product catalog data")
                        .version("1.0")
                )
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Server")
                ));
    }
}
