package com.obss.jcp.sinandogan.agileexpress.application.repository.interfaces;

import com.obss.jcp.sinandogan.agileexpress.domain.aggregates.project.Project;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository {
    void deleteById(UUID id);

    List<Project> findAll();

    Project save(Project project);

    Optional<Project> findById(UUID id);

    Optional<Project> findByBacklogTasksId(UUID taskId);

    Optional<Project> findByBacklogId(UUID backlogId);

    Optional<Project> findBySprintsId(UUID sprintId);

    List<Project> findByEmailOfManager(String emailOfManager);

    List<Project> findByEmailOfTeamLead(String emailOfTeamLead);

    List<Project> findByEmailsOfMembersContaining(String email);

    Optional<Project> findByIdAndEmailsOfMembersContaining(UUID projectId, String email);

    Optional<Project> findByIdAndEmailOfManager(UUID projectId, String email);

    Optional<Project> findByIdAndEmailOfTeamLead(UUID projectId, String email);

    List<Project> findActiveProjectsWithSprints(LocalDate date);
}
