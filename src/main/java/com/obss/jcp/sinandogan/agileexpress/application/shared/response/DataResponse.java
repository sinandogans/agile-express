package com.obss.jcp.sinandogan.agileexpress.application.shared.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class DataResponse<T> extends Response {
    private T data;
    public DataResponse(T data, String message, boolean success) {
        super(success, message);
        this.data = data;
    }
}
