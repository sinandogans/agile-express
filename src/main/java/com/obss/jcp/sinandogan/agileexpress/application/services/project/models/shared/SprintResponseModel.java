package com.obss.jcp.sinandogan.agileexpress.application.services.project.models.shared;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SprintResponseModel {
    UUID id;
    UUID projectId;
    int sprintNumber;
    boolean completed;
    boolean active;
    LocalDate startDate;
    LocalDate endDate;
    List<TaskResponseModel> tasks;
}
