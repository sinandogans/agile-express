package com.obss.jcp.sinandogan.agileexpress.application.services.project.exceptions;

import com.obss.jcp.sinandogan.agileexpress.application.shared.exceptions.BaseBusinessException;
import org.springframework.http.HttpStatus;

public class UserIsNotATeamLeadException extends BaseBusinessException {
    public UserIsNotATeamLeadException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public UserIsNotATeamLeadException(String message, String email) {
        super(message, HttpStatus.BAD_REQUEST);
        addExtension("email", email);
    }
}
