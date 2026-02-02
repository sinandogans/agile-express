package com.obss.jcp.sinandogan.agileexpress.infrastructure.project.entity.models;

import com.obss.jcp.sinandogan.agileexpress.domain.enums.StoryPoint;
import com.obss.jcp.sinandogan.agileexpress.domain.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "task")
public class TaskEntity {
    @Id
    private UUID id;
    private String name;
    private String description;
    private StoryPoint storyPoint;
    private String emailOfCreatedUser;
    private String emailOfAssignedUser;
    private int estimatedDuration;
    private TaskStatus status;
    @ManyToOne
    private BacklogEntity backlog;
    @ManyToOne
    private SprintEntity sprint;
    @ElementCollection
    @CollectionTable(name = "task_work_entries", joinColumns = @JoinColumn(name = "task_id"))
    private List<WorkEntryEntity> workEntries = new ArrayList<>();
}
