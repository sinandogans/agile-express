package com.obss.jcp.sinandogan.agileexpress.application.services.auth.exceptions;

import com.obss.jcp.sinandogan.agileexpress.application.shared.exceptions.BaseBusinessException;
import org.springframework.http.HttpStatus;

public class LoginFailedException extends BaseBusinessException {
    public LoginFailedException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public LoginFailedException(String message, String email) {
        super(message, HttpStatus.BAD_REQUEST);
        addExtension("email", email);
    }
}
