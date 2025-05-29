package com.zetta.forex.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ZettaForEx")
                        .description("Forex Exchange Rest App")
                        .version("v1.0"))
                .externalDocs(new ExternalDocumentation()
                        .description("Developed by andy489"));
    }

    @Bean
    public GroupedOpenApi restApi() {
        return GroupedOpenApi.builder()
                .group("rest-api")
                .pathsToMatch("/api/**")
                .packagesToScan("com.zetta.forex.rest")
                .build();
    }
}