package com.codewithfawaz.humanresourcemanagementsystem.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                description = "Human Resource Management System API Documentation - OpenAPI Documentation",
                title = "HR system API "
        ),
        security = {
                @SecurityRequirement(name = "bearerTokenAuth")
        }
)
@SecurityScheme(
        name = "bearerTokenAuth",
        type = SecuritySchemeType.HTTP,
        in = SecuritySchemeIn.HEADER,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfig {
}
