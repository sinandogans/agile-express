package com.obss.jcp.sinandogan.agileexpress.application.services.project.dtos.create;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateProjectRequestDto {
    @NotBlank
    String name;
    String description;
    @NotBlank
    @Email
    String emailOfManager;
    @Email
    String emailOfTeamLead;
    List<@Email String> emailsOfMembers;
    @FutureOrPresent
    LocalDate startDate;
    @FutureOrPresent
    LocalDate endDate;
    @Min(1)
    int sprintDurationInWeeks;
}
