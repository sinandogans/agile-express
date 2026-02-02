package com.obss.jcp.sinandogan.agileexpress.application.services.project.dtos.removemembers;

import jakarta.validation.constraints.Email;
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
public class RemoveMembersRequestDto {
    @NotNull
    UUID projectId;
    @NotNull
    List<@Email String> emails;
}
