package com.obss.jcp.sinandogan.agileexpress.application.services.project.sprint.models.start;

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
public class StartNextSprintModel {
    UUID projectId;
    LocalDate endDate;
}
