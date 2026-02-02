package com.obss.jcp.sinandogan.agileexpress.application.services.project.dtos.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateTeamLeadRequestDto {
    @NotNull
    UUID projectId;
    @NotBlank
    String emailOfTeamLead;
}