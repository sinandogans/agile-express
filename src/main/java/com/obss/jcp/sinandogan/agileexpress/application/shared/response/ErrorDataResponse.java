package com.obss.jcp.sinandogan.agileexpress.application.shared.response;

import lombok.Getter;

@Getter
public class ErrorDataResponse<T> extends DataResponse<T> {
    private T data;

    public ErrorDataResponse(T data, String message) {
        super(data, message, false);
    }
}
