package com.obss.jcp.sinandogan.agileexpress.infrastructure.project.mappers;

import com.obss.jcp.sinandogan.agileexpress.domain.aggregates.project.Task;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.project.entity.models.TaskEntity;

import java.util.UUID;
import java.util.stream.Collectors;

public class TaskMapper {
    public static Task toDomain(TaskEntity entity) {
        if (entity == null) return null;

        UUID backlogId = null;
        UUID sprintId = null;
        if (entity.getBacklog() != null) backlogId = entity.getBacklog().getId();
        if (entity.getSprint() != null) sprintId = entity.getSprint().getId();

        return new Task(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getStoryPoint(),
                entity.getEmailOfCreatedUser(),
                entity.getEmailOfAssignedUser(),
                entity.getEstimatedDuration(),
                entity.getStatus(),
                entity.getWorkEntries().stream()
                        .map(WorkEntryMapper::toDomain)
                        .collect(Collectors.toList()),
                backlogId,
                sprintId
        );
    }

    public static TaskEntity toEntity(Task domain) {
        if (domain == null) return null;

        TaskEntity entity = new TaskEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setStoryPoint(domain.getStoryPoint());
        entity.setEmailOfCreatedUser(domain.getEmailOfCreatedUser());
        entity.setEmailOfAssignedUser(domain.getEmailOfAssignedUser());
        entity.setEstimatedDuration(domain.getEstimatedDuration());
        entity.setStatus(domain.getStatus());

        entity.setWorkEntries(
                domain.getWorkEntries().stream()
                        .map(WorkEntryMapper::toEntity)
                        .collect(Collectors.toList())
        );

        return entity;
    }
}
