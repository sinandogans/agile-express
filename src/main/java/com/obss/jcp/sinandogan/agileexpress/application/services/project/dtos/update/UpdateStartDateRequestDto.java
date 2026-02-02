package com.obss.jcp.sinandogan.agileexpress.application.services.project.dtos.update;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateStartDateRequestDto {
    @NotNull
    UUID projectId;
    @NotNull
    @Future
    LocalDate startDate;
}