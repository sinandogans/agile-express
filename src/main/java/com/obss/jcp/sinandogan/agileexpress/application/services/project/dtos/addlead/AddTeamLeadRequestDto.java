package com.obss.jcp.sinandogan.agileexpress.application.services.project.dtos.addlead;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddTeamLeadRequestDto {
    @NotNull
    UUID projectId;
    @Email
    @NotBlank
    String emailOfTeamLead;
}
