package com.obss.jcp.sinandogan.agileexpress.application.services.project.exceptions;

import com.obss.jcp.sinandogan.agileexpress.application.shared.exceptions.BaseBusinessException;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

public class EndDateCannotBeBeforeStartDateException extends BaseBusinessException {
    public EndDateCannotBeBeforeStartDateException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public EndDateCannotBeBeforeStartDateException(String message, LocalDate requestedEndDate, LocalDate startDate) {
        super(message, HttpStatus.BAD_REQUEST);
        addExtension("requestedEndDate", requestedEndDate);
        addExtension("startDate", startDate);
    }
}
