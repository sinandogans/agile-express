package com.obss.jcp.sinandogan.agileexpress.application.services.project.exceptions;

import com.obss.jcp.sinandogan.agileexpress.application.shared.exceptions.BaseBusinessException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class ProjectAccessDeniedException extends BaseBusinessException {
    public ProjectAccessDeniedException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }

    public ProjectAccessDeniedException(String message, UUID projectId, String email) {
        super(message, HttpStatus.FORBIDDEN);
        addExtension("projectId", projectId);
        addExtension("email", email);
    }
}
