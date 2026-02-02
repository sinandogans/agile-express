package com.obss.jcp.sinandogan.agileexpress.application.services.project.dtos.update;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateSprintDurationRequestDto {
    @NotNull
    UUID projectId;
    @NotNull
    @Min(1)
    int sprintDurationInWeeks;
}