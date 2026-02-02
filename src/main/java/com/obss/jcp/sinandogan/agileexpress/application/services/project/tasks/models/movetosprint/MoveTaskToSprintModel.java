package com.obss.jcp.sinandogan.agileexpress.application.services.project.tasks.models.movetosprint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MoveTaskToSprintModel {
    private UUID taskId;
    private UUID sprintId;
}
