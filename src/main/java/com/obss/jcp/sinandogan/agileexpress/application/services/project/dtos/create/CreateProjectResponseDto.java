package com.obss.jcp.sinandogan.agileexpress.application.services.project.dtos.create;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateProjectResponseDto {
    UUID id;
    String name;
    String description;
    String emailOfManager;
    String emailOfTeamLead;
    List<String> emailsOfMembers;
    LocalDate startDate;
    LocalDate endDate;
    int sprintDurationInWeeks;
}
