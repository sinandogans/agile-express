package com.obss.jcp.sinandogan.agileexpress.application.services.project.models.update;

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
public class UpdateProjectModel {
    UUID projectId;
    String name;
    String description;
    String emailOfManager;
    String emailOfTeamLead;
    int sprintDurationInWeeks;
    LocalDate startDate;
    LocalDate endDate;
    List<String> emailsOfMembers;
}
