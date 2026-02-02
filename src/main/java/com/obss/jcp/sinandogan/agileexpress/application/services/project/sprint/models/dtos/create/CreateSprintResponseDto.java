package com.obss.jcp.sinandogan.agileexpress.application.services.project.sprint.models.dtos.create;

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
public class CreateSprintResponseDto {
    UUID id;
    int sprintNumber;
    boolean completed;
    boolean active;
    LocalDate startDate;
    LocalDate endDate;
}
