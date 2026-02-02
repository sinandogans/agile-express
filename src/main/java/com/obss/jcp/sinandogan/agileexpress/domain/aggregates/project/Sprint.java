package com.obss.jcp.sinandogan.agileexpress.domain.aggregates.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Sprint {
    private UUID id;
    private int sprintNumber;
    private boolean completed;
    private boolean active;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Task> tasks = new ArrayList<>();
    private UUID projectId;

    public static Sprint createSprint(int sprintNumber, boolean active, LocalDate startDate, LocalDate endDate) {
        var sprint = new Sprint();
        sprint.completed = false;
        sprint.active = active;
        sprint.sprintNumber = sprintNumber;
        sprint.startDate = startDate;
        sprint.endDate = endDate;
        return sprint;
    }

    void addTask(Task task) {
        if (tasks.contains(task)) {
            throw new IllegalArgumentException("The specified task is already in this sprint.");
        }
        tasks.add(task);
    }

    void removeTask(Task task) {
        if (tasks.contains(task)) {
            tasks.remove(task);
        } else {
            throw new IllegalArgumentException("The specified task is not in this sprint.");
        }
    }

    void updateDates(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("The specified start date is after the specified end date.");
        }
        this.startDate = startDate;
        this.endDate = endDate;
    }

    void start() {
        this.active = true;
    }

    void end() {
        this.active = false;
        this.completed = true;
    }

    void removeAllTasks() {
        tasks.forEach(Task::cleanSprintStatus);
        tasks.clear();
    }
}
