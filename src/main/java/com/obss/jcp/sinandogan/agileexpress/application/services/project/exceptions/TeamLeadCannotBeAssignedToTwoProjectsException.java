package com.obss.jcp.sinandogan.agileexpress.application.services.project.exceptions;

import com.obss.jcp.sinandogan.agileexpress.application.shared.exceptions.BaseBusinessException;
import org.springframework.http.HttpStatus;

public class TeamLeadCannotBeAssignedToTwoProjectsException extends BaseBusinessException {
    public TeamLeadCannotBeAssignedToTwoProjectsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }

    public TeamLeadCannotBeAssignedToTwoProjectsException(String message, String email) {
        super(message, HttpStatus.CONFLICT);
        addExtension("emailOfTeamLead", email);
    }
}
