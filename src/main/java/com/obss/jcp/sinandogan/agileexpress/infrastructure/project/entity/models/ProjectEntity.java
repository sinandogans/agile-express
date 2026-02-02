package com.obss.jcp.sinandogan.agileexpress.infrastructure.project.entity.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "project")
public class ProjectEntity {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String description;
    private String emailOfManager;
    private String emailOfTeamLead;
    @ElementCollection
    @CollectionTable(
            name = "project_members",
            joinColumns = @JoinColumn(name = "project_id")
    )
    @Column(name = "email")
    private List<String> emailsOfMembers;
    private LocalDate startDate;
    private LocalDate endDate;
    private int sprintDurationInWeeks;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private BacklogEntity backlog;
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sprintNumber ASC")
    private List<SprintEntity> sprints = new ArrayList<>();
}
