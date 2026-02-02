package com.obss.jcp.sinandogan.agileexpress.domain.aggregates.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Backlog {
    private UUID id;
    private List<Task> tasks = new ArrayList<>();
    private UUID projectId;

    public static Backlog createBacklogWithTasks(List<Task> tasks) {
        var backlog = new Backlog();
        backlog.tasks = tasks;
        return backlog;
    }

    public static Backlog createBacklog() {
        var backlog = new Backlog();
        return backlog;
    }

    void addTask(Task task) {
        this.tasks.add(task);
    }
}
