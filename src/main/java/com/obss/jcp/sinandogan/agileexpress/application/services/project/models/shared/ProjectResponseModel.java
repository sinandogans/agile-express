package com.obss.jcp.sinandogan.agileexpress.application.services.project.models.shared;

import com.obss.jcp.sinandogan.agileexpress.domain.enums.Role;
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
public class ProjectResponseModel {
    UUID id;
    String name;
    String description;
    String emailOfManager;
    String emailOfTeamLead;
    int sprintDurationInWeeks;
    LocalDate startDate;
    LocalDate endDate;
    List<String> emailsOfMembers;
    BacklogResponseModel backlog;
    List<SprintResponseModel> sprints;
}
