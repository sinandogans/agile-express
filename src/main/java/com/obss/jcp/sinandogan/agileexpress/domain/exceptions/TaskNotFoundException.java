package com.obss.jcp.sinandogan.agileexpress.domain.exceptions;

import java.util.UUID;

public class TaskNotFoundException extends BaseDomainException {
    public TaskNotFoundException(String message) {
        super(message, ErrorCode.NOT_FOUND);
    }

    public TaskNotFoundException(String message, UUID projectId, UUID taskId) {
        super(message, ErrorCode.NOT_FOUND);
        addExtension("projectId", projectId);
        addExtension("taskId", taskId);
    }
}
