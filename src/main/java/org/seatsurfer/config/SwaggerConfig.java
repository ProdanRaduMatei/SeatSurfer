package org.seatsurfer.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "SeatSurfer API",
                version = "v1",
                description = "API for seat booking"
        )
)
public class SwaggerConfig {
    // Pentru Springdoc OpenAPI Starter, nu mai avem nevoie de Docket sau @EnableOpenApi.
    // Springdoc va scana automat pachetul 'org.seatsurfer.web' și va genera documentația.
}