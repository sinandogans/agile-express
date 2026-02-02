package com.obss.jcp.sinandogan.agileexpress.domain.exceptions;

import lombok.Getter;
import java.util.HashMap;
import java.util.Map;

@Getter
public class BaseDomainException extends RuntimeException {
    private final Map<String, Object> extensions = new HashMap<>();
    private final ErrorCode errorCode;

    public BaseDomainException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public void addExtension(String key, Object value) {
        extensions.put(key, value);
    }
}
