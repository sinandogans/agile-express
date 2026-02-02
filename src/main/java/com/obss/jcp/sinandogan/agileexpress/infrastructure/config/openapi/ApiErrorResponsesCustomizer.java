package com.obss.jcp.sinandogan.agileexpress.infrastructure.config.openapi;

import com.obss.jcp.sinandogan.agileexpress.infrastructure.config.exceptionhandling.ApiError;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class ApiErrorResponsesCustomizer implements OperationCustomizer {

    @Override
    public io.swagger.v3.oas.models.Operation customize(
            io.swagger.v3.oas.models.Operation operation,
            HandlerMethod handlerMethod) {

        Method method = handlerMethod.getMethod();
        ApiErrorResponses annotation = method.getAnnotation(ApiErrorResponses.class);

        if (annotation != null) {
            Set<Integer> errorCodes = new HashSet<>();

            Arrays.stream(annotation.value()).forEach(errorCodes::add);

            errorCodes.forEach(code -> {
                operation.getResponses().addApiResponse(
                    String.valueOf(code),
                    createErrorResponse(getErrorDescription(code), code)
                );
            });
        }

        return operation;
    }

    private ApiResponse createErrorResponse(String description, int status) {
        return new ApiResponse()
                .description(description)
                .content(new Content().addMediaType(
                        org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                        new MediaType().schema(new Schema<>().$ref("#/components/schemas/ApiError"))
                ));
    }

    private String getErrorDescription(int statusCode) {
        return switch (statusCode) {
            case 400 -> "Bad request / Validation error";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Resource not found";
            case 409 -> "Conflict";
            case 422 -> "Unprocessable Entity";
            case 500 -> "Internal server error";
            default -> "Error";
        };
    }
}
