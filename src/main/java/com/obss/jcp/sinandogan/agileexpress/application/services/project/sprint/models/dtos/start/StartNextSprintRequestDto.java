package com.obss.jcp.sinandogan.agileexpress.application.services.project.sprint.models.dtos.start;

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
public class StartNextSprintRequestDto {
    @NotNull
    UUID projectId;
    @NotNull
    LocalDate endDate;
}
