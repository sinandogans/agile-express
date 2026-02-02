package com.obss.jcp.sinandogan.agileexpress.infrastructure.config.exceptionhandling;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name= "ApiError", description = "Standard Error Response Wrapper")
public class ApiError {
    @Schema(description = "Error message", example = "Resource not found")
    private String message;
    @Schema(description = "HTTP status code", example = "404")
    private int status;
    @Schema(description = "Timestamp of the error", example = "2021-03-22T12:00:00")
    private LocalDateTime timestamp;
    @Schema(description = "Request Path where the error occurred", example = "/api/projects/123")
    private String path;
    @Schema(description = "Additional details about the error (key-value pair)")
    private Map<String, Object> details;
}
