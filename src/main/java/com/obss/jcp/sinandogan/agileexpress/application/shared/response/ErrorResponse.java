package com.obss.jcp.sinandogan.agileexpress.application.shared.response;


public class ErrorResponse extends Response {
    public ErrorResponse(String message) {
        super(false, message);
    }
}
