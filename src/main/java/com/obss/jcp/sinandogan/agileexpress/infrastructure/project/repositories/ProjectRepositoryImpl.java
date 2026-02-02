package com.obss.jcp.sinandogan.agileexpress.infrastructure.project.repositories;

import com.obss.jcp.sinandogan.agileexpress.application.repository.interfaces.ProjectRepository;
import com.obss.jcp.sinandogan.agileexpress.domain.aggregates.project.Project;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.project.mappers.ProjectMapper;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.project.entity.models.ProjectEntity;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.project.repositories.jpa.JPAProjectRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class ProjectRepositoryImpl implements ProjectRepository {

    private final JPAProjectRepository jpaRepository;

    public ProjectRepositoryImpl(JPAProjectRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<Project> findAll() {
        return jpaRepository.findAll().stream()
                .map(ProjectMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Project save(Project project) {
        ProjectEntity entity = ProjectMapper.toEntity(project);
        jpaRepository.save(entity);
        return ProjectMapper.toDomain(entity);
    }

    @Override
    public Optional<Project> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(ProjectMapper::toDomain);
    }

    @Override
    public Optional<Project> findByBacklogTasksId(UUID taskId) {
        return jpaRepository.findByBacklogTasksId(taskId)
                .map(ProjectMapper::toDomain);
    }

    @Override
    public Optional<Project> findByBacklogId(UUID backlogId) {
        return jpaRepository.findByBacklogId(backlogId)
                .map(ProjectMapper::toDomain);
    }

    @Override
    public Optional<Project> findBySprintsId(UUID sprintId) {
        return jpaRepository.findBySprintsId(sprintId)
                .map(ProjectMapper::toDomain);
    }

    @Override
    public List<Project> findByEmailOfManager(String emailOfManager) {
        return jpaRepository.getByEmailOfManager(emailOfManager).stream()
                .map(ProjectMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Project> findByEmailOfTeamLead(String emailOfTeamLead) {
        return jpaRepository.getByEmailOfTeamLead(emailOfTeamLead).stream()
                .map(ProjectMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Project> findByEmailsOfMembersContaining(String email) {
        return jpaRepository.findByEmailsOfMembersContaining(email).stream()
                .map(ProjectMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Project> findByIdAndEmailsOfMembersContaining(UUID projectId, String email) {
        return jpaRepository.findByIdAndEmailsOfMembersContaining(projectId, email)
                .map(ProjectMapper::toDomain);
    }

    @Override
    public Optional<Project> findByIdAndEmailOfManager(UUID projectId, String email) {
        return jpaRepository.findByIdAndEmailOfManager(projectId, email)
                .map(ProjectMapper::toDomain);
    }

    @Override
    public Optional<Project> findByIdAndEmailOfTeamLead(UUID projectId, String email) {
        return jpaRepository.findByIdAndEmailOfTeamLead(projectId, email)
                .map(ProjectMapper::toDomain);
    }


    @Override
    public List<Project> findActiveProjectsWithSprints(LocalDate date) {
        List<ProjectEntity> entities = jpaRepository.findActiveProjectsWithSprints(date);
        return entities.stream()
                .map(ProjectMapper::toDomain)
                .collect(Collectors.toList());
    }
}
