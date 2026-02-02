package com.obss.jcp.sinandogan.agileexpress.domain.aggregates.project;

import com.obss.jcp.sinandogan.agileexpress.domain.enums.StoryPoint;
import com.obss.jcp.sinandogan.agileexpress.domain.enums.TaskStatus;
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
public class Task {
    private UUID id = UUID.randomUUID();
    private String name;
    private String description;
    private StoryPoint storyPoint;
    private String emailOfCreatedUser;
    private String emailOfAssignedUser;
    private int estimatedDuration;
    private TaskStatus status;
    private List<WorkEntry> workEntries = new ArrayList<>();
    private UUID backlogId;
    private UUID sprintId;

    public static Task createTask(String name, String description, StoryPoint storyPoint, String emailOfCreatedUser, String emailOfAssignedUser, int estimatedDuration) {
        var task = new Task();
        task.name = name;
        task.description = description;
        task.storyPoint = storyPoint;
        task.emailOfCreatedUser = emailOfCreatedUser;
        task.emailOfAssignedUser = emailOfAssignedUser;
        task.estimatedDuration = estimatedDuration;
        task.status = TaskStatus.UNASSIGNED;
        return task;
    }

    void logWork(LocalDate date, int hours) {
        workEntries.add(new WorkEntry(date, hours));
    }

    void updateName(String newName) {
        if (newName.isEmpty()) {
            throw new IllegalArgumentException("The specified name cannot be empty.");
        }
        this.name = newName;
    }

    void updateDescription(String newDescription) {
        if (newDescription.isEmpty()) {
            throw new IllegalArgumentException("The specified description cannot be empty.");
        }
        this.description = newDescription;
    }

    void updateStoryPoint(StoryPoint storyPoint) {
        if (storyPoint == null) {
            throw new IllegalArgumentException("The specified story point cannot be null.");
        }
        this.storyPoint = storyPoint;
    }

    void updateAssignedUser(String emailOfAssignedUser) {
        this.status = TaskStatus.ASSIGNED;
        if (emailOfAssignedUser == null || emailOfAssignedUser.isEmpty()) {
            this.status = TaskStatus.UNASSIGNED;
        }
        this.emailOfAssignedUser = emailOfAssignedUser;
    }

    void updateEstimatedDuration(int estimatedDuration) {
        if (estimatedDuration < 0) {
            throw new IllegalArgumentException("The specified estimated duration cannot be negative.");
        }
        this.estimatedDuration = estimatedDuration;
    }

    void updateStatus(TaskStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("The specified status cannot be null.");
        }
        this.status = status;
    }

    void cleanSprintStatus() {
        this.status = TaskStatus.UNASSIGNED;
        this.emailOfAssignedUser = null;
    }

    public static class Builder {
        private final String name;
        private final String description;
        private final String emailOfCreatedUser;

        private StoryPoint storyPoint;
        private String emailOfAssignedUser;
        private int estimatedDuration;

        public Builder(String name, String description, String emailOfCreatedUser) {
            this.name = name;
            this.description = description;
            this.emailOfCreatedUser = emailOfCreatedUser;
        }

        public Task.Builder storyPoint(StoryPoint storyPoint) {
            this.storyPoint = storyPoint;
            return this;
        }

        public Task.Builder emailOfAssignedUser(String emailOfAssignedUser) {
            this.emailOfAssignedUser = emailOfAssignedUser;
            return this;
        }

        public Task.Builder estimatedDuration(int estimatedDuration) {
            this.estimatedDuration = estimatedDuration;
            return this;
        }

        public Task build() {
            Task task = Task.createTask(name, description, storyPoint, emailOfCreatedUser, emailOfAssignedUser, estimatedDuration);
            return task;
        }
    }
}
