package com.obss.jcp.sinandogan.agileexpress.application.services.project.exceptions;

import com.obss.jcp.sinandogan.agileexpress.application.shared.exceptions.BaseBusinessException;
import org.springframework.http.HttpStatus;

public class ProjectMemberCannotBeAssignedToTwoProjectsException extends BaseBusinessException {
    public ProjectMemberCannotBeAssignedToTwoProjectsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }

    public ProjectMemberCannotBeAssignedToTwoProjectsException(String message, String email) {
        super(message, HttpStatus.CONFLICT);
        addExtension("emailOfProjectMember", email);
    }
}
