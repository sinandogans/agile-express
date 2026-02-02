package com.obss.jcp.sinandogan.agileexpress.infrastructure.config.openapi;

import com.obss.jcp.sinandogan.agileexpress.infrastructure.config.exceptionhandling.ApiError;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                        .title("Agile Express API")
                        .version("1.0.0")
                        .description("Agile Express API Documentation"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"))
                        .addSchemas("ApiError", createApiErrorSchema("Standard Error Response Wrapper", 500)))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }

    @Bean
    public OpenApiCustomizer globalErrorResponseCustomizer() {
        return openApi -> {
            openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
                ApiResponses responses = operation.getResponses();
                responses.addApiResponse("500", createErrorResponse("Internal server error", 500));
            }));
        };
    }

    private ApiResponse createErrorResponse(String description, int status) {
        return new ApiResponse()
                .description(description)
                .content(new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                        new MediaType().schema(createApiErrorSchema(description, status))));
    }

    private Schema<?> createApiErrorSchema(String message, int status) {
        return new Schema<>()
                .type("object")
                .name("ApiError")
                .description("Standard Error Response Wrapper")
                .addProperty("message", new StringSchema()
                        .description("Error message")
                        .example(message))
                .addProperty("status", new IntegerSchema()
                        .description("HTTP status code")
                        .example(status))
                .addProperty("timestamp", new StringSchema()
                        .description("Timestamp of the error")
                        .example("2021-03-22T12:00:00")
                        .format("date-time"))
                .addProperty("path", new StringSchema()
                        .description("Request Path where the error occurred")
                        .example("/api/projects/123"))
                .addProperty("details", new MapSchema()
                        .description("Details about the error (key-value pairs)")
                        .example(Map.of("x", 1, "y", "z")));
    }
}
