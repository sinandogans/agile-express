package com.obss.jcp.sinandogan.agileexpress.application.services.project.exceptions;

import com.obss.jcp.sinandogan.agileexpress.application.shared.exceptions.BaseBusinessException;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.UUID;

public class EndDateOfFinishedProjectCannotBeChangedException extends BaseBusinessException {
    public EndDateOfFinishedProjectCannotBeChangedException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public EndDateOfFinishedProjectCannotBeChangedException(String message, UUID projectId, LocalDate endDate) {
        super(message, HttpStatus.BAD_REQUEST);
        addExtension("projectId", projectId);
        addExtension("endDate", endDate);
    }
}
