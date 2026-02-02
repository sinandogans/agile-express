package com.obss.jcp.sinandogan.agileexpress.application.services.project.tasks.models.dtos.removefromsprint;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RemoveTaskFromSprintRequestDto {
    @NotNull
    UUID taskId;
}
