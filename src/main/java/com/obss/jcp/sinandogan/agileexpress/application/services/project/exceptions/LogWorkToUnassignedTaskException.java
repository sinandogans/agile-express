package com.obss.jcp.sinandogan.agileexpress.application.services.project.exceptions;

import com.obss.jcp.sinandogan.agileexpress.application.shared.exceptions.BaseBusinessException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class LogWorkToUnassignedTaskException extends BaseBusinessException {
    public LogWorkToUnassignedTaskException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }

    public LogWorkToUnassignedTaskException(String message, UUID projectId, UUID taskId) {
        super(message, HttpStatus.FORBIDDEN);
        addExtension("projectId", projectId);
        addExtension("taskId", taskId);
    }
}
