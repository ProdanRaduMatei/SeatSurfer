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
public class OpenApiConfig {
    // poți adăuga aici și alte configurări speciale de OpenAPI
}