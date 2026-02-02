package com.obss.jcp.sinandogan.agileexpress.infrastructure.project.repositories.jpa;

import com.obss.jcp.sinandogan.agileexpress.infrastructure.project.entity.models.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JPAProjectRepository extends JpaRepository<ProjectEntity, UUID> {

    ProjectEntity save(ProjectEntity projectEntity);

    Optional<ProjectEntity> findById(UUID id);

    Optional<ProjectEntity> findByBacklogTasksId(UUID taskId);

    Optional<ProjectEntity> findByBacklogId(UUID backlogId);

    Optional<ProjectEntity> findBySprintsId(UUID sprintId);

    List<ProjectEntity> getByEmailOfManager(String emailOfManager);

    List<ProjectEntity> getByEmailOfTeamLead(String emailOfTeamLead);

    List<ProjectEntity> findByEmailsOfMembersContaining(String email);

    Optional<ProjectEntity> findByIdAndEmailsOfMembersContaining(UUID projectId, String email);

    Optional<ProjectEntity> findByIdAndEmailOfManager(UUID projectId, String email);

    Optional<ProjectEntity> findByIdAndEmailOfTeamLead(UUID projectId, String email);

//    @Query(value = """
//            SELECT * FROM project
//            WHERE to_tsvector('simple', name || ' ' || description)
//                  @@ to_tsquery('simple', :query)
//            """, nativeQuery = true)
//    List<ProjectEntity> searchProjects(@Param("query") String query);

    @Query("""
                SELECT DISTINCT p FROM ProjectEntity p
                JOIN FETCH p.sprints s
                WHERE p.sprints IS NOT EMPTY
                  AND (p.endDate IS NULL OR p.endDate >= :date)
            """)
    List<ProjectEntity> findActiveProjectsWithSprints(LocalDate date);

}
