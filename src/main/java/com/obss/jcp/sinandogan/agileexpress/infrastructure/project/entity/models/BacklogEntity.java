package com.obss.jcp.sinandogan.agileexpress.infrastructure.project.entity.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "backlog")
public class BacklogEntity {
    @Id
    @GeneratedValue
    private UUID id;
    @OneToOne
    private ProjectEntity project;
    @OneToMany(mappedBy = "backlog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskEntity> tasks = new ArrayList<>();
}
