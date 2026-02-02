package com.obss.jcp.sinandogan.agileexpress.application.services.project.tasks.models.logwork;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LogWorkModel {
    private UUID taskId;
    private LocalDate date;
    private int hours;
    private String email;
}
