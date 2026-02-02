package com.obss.jcp.sinandogan.agileexpress.application.services.project.dtos.update;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateDescriptionRequestDto {
    @NotNull
    UUID projectId;
    @NotBlank
    String description;
}