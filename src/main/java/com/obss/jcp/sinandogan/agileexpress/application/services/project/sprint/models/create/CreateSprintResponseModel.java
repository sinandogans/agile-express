package com.obss.jcp.sinandogan.agileexpress.application.services.project.sprint.models.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreateSprintResponseModel {
    UUID id;
    int sprintNumber;
    boolean completed;
    boolean active;
    LocalDate startDate;
    LocalDate endDate;
}
