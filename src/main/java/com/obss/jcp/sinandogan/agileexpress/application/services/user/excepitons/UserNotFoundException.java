package com.obss.jcp.sinandogan.agileexpress.application.services.user.excepitons;

import com.obss.jcp.sinandogan.agileexpress.application.shared.exceptions.BaseBusinessException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BaseBusinessException {
    public UserNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public UserNotFoundException(String message, String email) {
        super(message, HttpStatus.NOT_FOUND);
        addExtension("email", email);
    }
}
