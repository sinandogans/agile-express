package com.obss.jcp.sinandogan.agileexpress.application.shared.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
public class BaseBusinessException extends RuntimeException {
    private final Map<String, Object> extensions = new HashMap<>();
    private final HttpStatus httpStatus;

    public BaseBusinessException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public void addExtension(String key, Object value) {
        extensions.put(key, value);
    }
}
