package com.obss.jcp.sinandogan.agileexpress.application.shared.response;

import lombok.Getter;

@Getter
public class SuccessDataResponse<T> extends DataResponse<T> {
    private T data;

    public SuccessDataResponse(T data, String message) {
        super(data, message, true);
    }
}
