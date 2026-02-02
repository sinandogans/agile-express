package com.obss.jcp.sinandogan.agileexpress.infrastructure.project.mappers;

import com.obss.jcp.sinandogan.agileexpress.domain.aggregates.project.Backlog;
import com.obss.jcp.sinandogan.agileexpress.domain.aggregates.project.Project;
import com.obss.jcp.sinandogan.agileexpress.domain.aggregates.project.Sprint;
import com.obss.jcp.sinandogan.agileexpress.domain.aggregates.project.Task;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.project.entity.models.BacklogEntity;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.project.entity.models.ProjectEntity;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.project.entity.models.SprintEntity;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.project.entity.models.TaskEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectMapper {

    public static Project toDomain(ProjectEntity entity) {
        if (entity == null) return null;

        List<TaskEntity> allTaskEntities = new ArrayList<>();
        allTaskEntities.addAll(entity.getBacklog().getTasks());
        entity.getSprints().forEach(sprint -> allTaskEntities.addAll(sprint.getTasks()));

        var taskDomainMap = allTaskEntities.stream()
                .distinct()
                .map(TaskMapper::toDomain)
                .collect(Collectors.toMap(Task::getId, t -> t));

        List<Task> backlogTasks = entity.getBacklog().getTasks().stream()
                .map(taskEntity -> taskDomainMap.get(taskEntity.getId()))
                .collect(Collectors.toList());
        Backlog backlog = new Backlog(entity.getBacklog().getId(), backlogTasks, entity.getId());

        List<Sprint> sprints = entity.getSprints().stream()
                .map(sprintEntity -> {
                    List<Task> sprintTasks = sprintEntity.getTasks().stream()
                            .map(taskEntity -> taskDomainMap.get(taskEntity.getId()))
                            .collect(Collectors.toList());
                    return new Sprint(
                            sprintEntity.getId(),
                            sprintEntity.getSprintNumber(),
                            sprintEntity.isCompleted(),
                            sprintEntity.isActive(),
                            sprintEntity.getStartDate(),
                            sprintEntity.getEndDate(),
                            sprintTasks,
                            entity.getId()
                    );
                })
                .collect(Collectors.toList());

        return new Project(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getEmailOfManager(),
                entity.getEmailOfTeamLead(),
                entity.getEmailsOfMembers(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getSprintDurationInWeeks(),
                backlog,
                sprints
        );
    }

    public static ProjectEntity toEntity(Project domain) {
        if (domain == null) return null;

        ProjectEntity entity = new ProjectEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setEmailOfManager(domain.getEmailOfManager());
        entity.setEmailOfTeamLead(domain.getEmailOfTeamLead());
        entity.setEmailsOfMembers(domain.getEmailsOfMembers());
        entity.setStartDate(domain.getStartDate());
        entity.setEndDate(domain.getEndDate());
        entity.setSprintDurationInWeeks(domain.getSprintDurationInWeeks());

        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(domain.getBacklog().getTasks());
        domain.getSprints().forEach(sprint -> allTasks.addAll(sprint.getTasks()));

        var taskEntityMap = allTasks.stream()
                .distinct()
                .map(TaskMapper::toEntity)
                .collect(Collectors.toMap(TaskEntity::getId, t -> t));

        BacklogEntity backlogEntity = new BacklogEntity();
        backlogEntity.setId(domain.getBacklog().getId());
        backlogEntity.setProject(entity);

        List<TaskEntity> backlogTasks = domain.getBacklog().getTasks().stream()
                .map(task -> {
                    TaskEntity te = taskEntityMap.get(task.getId());
                    te.setBacklog(backlogEntity); // backlink set et
                    return te;
                })
                .collect(Collectors.toList());
        backlogEntity.setTasks(backlogTasks);
        entity.setBacklog(backlogEntity);

        List<SprintEntity> sprintEntities = domain.getSprints().stream()
                .map(sprint -> {
                    SprintEntity sprintEntity = new SprintEntity();
                    sprintEntity.setId(sprint.getId());
                    sprintEntity.setSprintNumber(sprint.getSprintNumber());
                    sprintEntity.setCompleted(sprint.isCompleted());
                    sprintEntity.setActive(sprint.isActive());
                    sprintEntity.setStartDate(sprint.getStartDate());
                    sprintEntity.setEndDate(sprint.getEndDate());
                    sprintEntity.setProject(entity);

                    List<TaskEntity> sprintTasks = sprint.getTasks().stream()
                            .map(task -> {
                                TaskEntity te = taskEntityMap.get(task.getId());
                                te.setSprint(sprintEntity);
                                return te;
                            })
                            .collect(Collectors.toList());

                    sprintEntity.setTasks(sprintTasks);
                    return sprintEntity;
                })
                .collect(Collectors.toList());

        entity.setSprints(sprintEntities);

        return entity;
    }
}

