package com.obss.jcp.sinandogan.agileexpress.application.services.project.exceptions;

import com.obss.jcp.sinandogan.agileexpress.application.shared.exceptions.BaseBusinessException;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.UUID;

public class StartDateOfStartedProjectCannotBeChangedException extends BaseBusinessException {
    public StartDateOfStartedProjectCannotBeChangedException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public StartDateOfStartedProjectCannotBeChangedException(String message, UUID projectId, LocalDate startDate) {
        super(message, HttpStatus.BAD_REQUEST);
        addExtension("projectId", projectId);
        addExtension("startDate", startDate);
    }
}
