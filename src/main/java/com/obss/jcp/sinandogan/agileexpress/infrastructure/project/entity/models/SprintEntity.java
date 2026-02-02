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
@Table(name = "sprint")
public class SprintEntity {
    @Id
    @GeneratedValue
    private UUID id;
    private int sprintNumber;
    private boolean completed;
    private boolean active;
    private LocalDate startDate;
    private LocalDate endDate;
    @ManyToOne
    private ProjectEntity project;
    @OneToMany(mappedBy = "sprint", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<TaskEntity> tasks = new ArrayList<>();
}
