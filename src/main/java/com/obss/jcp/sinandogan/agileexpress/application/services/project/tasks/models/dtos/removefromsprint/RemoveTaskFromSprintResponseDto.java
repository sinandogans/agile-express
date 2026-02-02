package com.obss.jcp.sinandogan.agileexpress.application.services.project.tasks.models.dtos.removefromsprint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RemoveTaskFromSprintResponseDto {
    UUID taskId;
    UUID sprintId;
}
