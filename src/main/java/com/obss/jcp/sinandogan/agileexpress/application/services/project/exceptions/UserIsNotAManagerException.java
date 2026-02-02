package com.obss.jcp.sinandogan.agileexpress.application.services.project.exceptions;

import com.obss.jcp.sinandogan.agileexpress.application.shared.exceptions.BaseBusinessException;
import org.springframework.http.HttpStatus;

public class UserIsNotAManagerException extends BaseBusinessException {
    public UserIsNotAManagerException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public UserIsNotAManagerException(String message, String email) {
        super(message, HttpStatus.BAD_REQUEST);
        addExtension("email", email);
    }
}
