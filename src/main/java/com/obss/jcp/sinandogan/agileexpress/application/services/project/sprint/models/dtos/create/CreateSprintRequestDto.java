package com.obss.jcp.sinandogan.agileexpress.application.services.project.sprint.models.dtos.create;

import jakarta.validation.constraints.FutureOrPresent;
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
public class CreateSprintRequestDto {
    @NotNull
    UUID projectId;
}
