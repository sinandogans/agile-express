package com.obss.jcp.sinandogan.agileexpress.domain.exceptions;

import java.util.UUID;

public class SprintNotFoundException extends BaseDomainException {
    public SprintNotFoundException(String message) {
        super(message, ErrorCode.NOT_FOUND);
    }

    public SprintNotFoundException(String message, UUID projectId, int sprintNumber) {
        super(message, ErrorCode.NOT_FOUND);
        addExtension("projectId", projectId);
        addExtension("sprintNumber", sprintNumber);
    }
}
