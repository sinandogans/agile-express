package com.obss.jcp.sinandogan.agileexpress.application.repository.interfaces;

import com.obss.jcp.sinandogan.agileexpress.domain.aggregates.email.EmailTask;

import java.time.LocalDate;
import java.util.Optional;

public interface EmailTaskRepository {
    Optional<EmailTask> findByDate(LocalDate date);

    EmailTask save(EmailTask emailTask);
}
