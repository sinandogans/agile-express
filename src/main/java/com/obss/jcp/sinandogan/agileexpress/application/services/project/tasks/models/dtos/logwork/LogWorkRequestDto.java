package com.obss.jcp.sinandogan.agileexpress.application.services.project.tasks.models.dtos.logwork;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LogWorkRequestDto {
    @NotNull
    private UUID taskId;
    @NotNull
    private LocalDate date;
    private int hours;
}
