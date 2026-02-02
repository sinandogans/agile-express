package com.obss.jcp.sinandogan.agileexpress.application.services.project.exceptions;

import com.obss.jcp.sinandogan.agileexpress.application.shared.exceptions.BaseBusinessException;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

public class StartDateCannotBeAfterEndDateException extends BaseBusinessException {
    public StartDateCannotBeAfterEndDateException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public StartDateCannotBeAfterEndDateException(String message, LocalDate requestedStartDate, LocalDate endDate) {
        super(message, HttpStatus.BAD_REQUEST);
        addExtension("requestedStartDate", requestedStartDate);
        addExtension("endDate", endDate);
    }
}
