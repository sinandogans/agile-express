package com.obss.jcp.sinandogan.agileexpress.application.services.project.exceptions;

import com.obss.jcp.sinandogan.agileexpress.application.shared.exceptions.BaseBusinessException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class ProjectNotFoundException extends BaseBusinessException {
    public ProjectNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public ProjectNotFoundException(String message, UUID projectId) {
        super(message, HttpStatus.NOT_FOUND);
        addExtension("projectId", projectId);
    }
}
