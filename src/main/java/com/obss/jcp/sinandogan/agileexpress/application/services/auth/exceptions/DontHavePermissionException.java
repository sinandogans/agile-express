package com.obss.jcp.sinandogan.agileexpress.application.services.auth.exceptions;

import com.obss.jcp.sinandogan.agileexpress.application.shared.exceptions.BaseBusinessException;
import org.springframework.http.HttpStatus;

public class DontHavePermissionException extends BaseBusinessException {
    public DontHavePermissionException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }

    public DontHavePermissionException(String message, String email) {
        super(message, HttpStatus.FORBIDDEN);
        addExtension("email", email);
    }
}
