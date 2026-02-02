package com.obss.jcp.sinandogan.agileexpress.domain.aggregates.project;

import com.obss.jcp.sinandogan.agileexpress.domain.enums.StoryPoint;
import com.obss.jcp.sinandogan.agileexpress.domain.enums.TaskStatus;
import com.obss.jcp.sinandogan.agileexpress.domain.exceptions.SprintNotFoundException;
import com.obss.jcp.sinandogan.agileexpress.domain.exceptions.TaskNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    private UUID id;
    private String name;
    private String description;
    private String emailOfManager;
    private String emailOfTeamLead;
    private List<String> emailsOfMembers = new ArrayList<>();
    private LocalDate startDate;
    private LocalDate endDate;
    private int sprintDurationInWeeks;
    private Backlog backlog;
    private List<Sprint> sprints = new ArrayList<>();

    public List<String> getEmailsOfMembers() {
        return Collections.unmodifiableList(emailsOfMembers);
    }

    public Sprint createSprint() {
        LocalDate startDate;
        boolean active = false;
        if (sprints.isEmpty()) {
            if (this.startDate == null) {
                throw new RuntimeException("Please set the start date of the project before creating the first sprint.");
            }
            startDate = this.startDate;
        } else {
            Optional<Sprint> maxSprint = sprints.stream()
                    .max(Comparator.comparingInt(Sprint::getSprintNumber));
            startDate = maxSprint.get().getEndDate().plusDays(1);
        }
        if (startDate.isBefore(LocalDate.now())) {
            active = true;
        }
        var sprint = Sprint.createSprint(sprints.size() + 1, active, startDate, startDate.plusWeeks(sprintDurationInWeeks).minusDays(1));
        this.sprints.add(sprint);
        return sprint;
    }

    private void createBacklog() {
        this.backlog = Backlog.createBacklog();
    }

    public void logWork(UUID taskId, LocalDate date, int hours) {
        Task task = getTaskById(taskId);
        if (task.getStatus() != TaskStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("Task is not in progress.");
        }
        task.logWork(date, hours);
    }

    public Task addTask(String name, String description, String emailOfCreatedUser, StoryPoint storyPoint, String emailOfAssignedUser, int estimatedDuration) {
        var builder = new Task.Builder(name, description, emailOfCreatedUser);
        if (storyPoint != null) {
            builder.storyPoint(storyPoint);
        }
        if (emailOfAssignedUser != null && !emailOfAssignedUser.isEmpty()) {
            builder.emailOfAssignedUser(emailOfAssignedUser);
        }
        if (estimatedDuration != 0) {
            builder.estimatedDuration(estimatedDuration);
        }
        Task task = builder.build();
        backlog.addTask(task);
        return task;
    }

    public void addMember(String email) {
        if (emailOfManager != null && emailOfManager.equals(email)) {
            throw new IllegalArgumentException("Project manager cannot be added as a member.");
        }
        if (emailOfTeamLead != null && emailOfTeamLead.equals(email)) {
            throw new IllegalArgumentException("Project team lead cannot be added as a member.");
        }
        emailsOfMembers.add(email);
    }

    public void deleteMember(String email) {
        if (emailsOfMembers.contains(email)) {
            emailsOfMembers.remove(email);
            updateTasksOfRemovedMember(email);
        }
    }

    private void updateTasksOfRemovedMember(String email) {
        var tasksOfRemovedMember = backlog.getTasks().stream().filter(task -> task.getEmailOfAssignedUser().equals(email)).toList();
        tasksOfRemovedMember.forEach(Task::cleanSprintStatus);
        sprints.forEach(sprint -> sprint.getTasks().removeAll(tasksOfRemovedMember));
    }

    public void deleteTask(UUID taskId) {
        boolean removedFromBacklog = backlog.getTasks().removeIf(task -> task.getId().equals(taskId));
        if (!removedFromBacklog) {
            throw new TaskNotFoundException("Task could not found in backlog", this.id, taskId);
        }
        sprints.forEach(sprint -> sprint.getTasks().removeIf(task -> task.getId().equals(taskId)));
    }

    public void moveTaskToSprint(UUID taskId, UUID sprintId) {
        Task task = backlog.getTasks().stream().filter(t -> t.getId().equals(taskId)).findFirst().orElseThrow(() -> new TaskNotFoundException("Task could not found in backlog.", this.id, taskId));
        Sprint sprint = getSprintById(sprintId);
        sprint.addTask(task);
    }

    public Sprint getLastSprint() {
        if (sprints.isEmpty()) {
            throw new RuntimeException("There is no sprint in this project.");
        }
        return sprints.getFirst();
    }

    public Sprint getSprintById(UUID sprintId) {
        if (sprints.isEmpty()) {
            throw new RuntimeException("There is no sprint in this project.");
        }
        return sprints.stream().filter(sprint -> sprint.getId().equals(sprintId)).findFirst().orElseThrow(() -> new RuntimeException("The specified sprint could not found in this project."));
    }

    public void removeTaskFromSprint(UUID taskId) {
        Task task = backlog.getTasks().stream().filter(t -> t.getId().equals(taskId)).findFirst().orElseThrow(() -> new RuntimeException("Task could not found in backlog"));
        Sprint sprint = getLastSprint();
        sprint.removeTask(task);
        task.cleanSprintStatus();
    }

    public void updateTaskName(UUID taskId, String newName) {
        Task task = backlog.getTasks().stream().filter(t -> t.getId().equals(taskId)).findFirst().orElseThrow(() -> new RuntimeException("Task could not found in backlog"));
        task.updateName(newName);
    }

    public void updateTaskDescription(UUID taskId, String newDescription) {
        Task task = backlog.getTasks().stream().filter(t -> t.getId().equals(taskId)).findFirst().orElseThrow(() -> new RuntimeException("Task could not found in backlog"));
        task.updateDescription(newDescription);
    }

    public void updateTaskStoryPoint(UUID taskId, StoryPoint storyPoint) {
        Task task = backlog.getTasks().stream().filter(t -> t.getId().equals(taskId)).findFirst().orElseThrow(() -> new RuntimeException("Task could not found in backlog"));
        if (!sprints.getFirst().getTasks().contains(task)) {
            throw new IllegalArgumentException("Cannot add a story point to a task if task is not in the current sprint.");
        }
        task.updateStoryPoint(storyPoint);
    }

    public void updateTaskAssignedUser(UUID taskId, String emailOfAssignedUser) {
        Task task = backlog.getTasks().stream().filter(t -> t.getId().equals(taskId)).findFirst().orElseThrow(() -> new RuntimeException("Task could not found in backlog"));
        if (emailOfAssignedUser != null && task.getSprintId() == null) {
            throw new IllegalArgumentException("Cannot assign a task to a member if task is not in the current sprint.");
        }
        if (emailOfAssignedUser == null || emailOfAssignedUser.isEmpty()) {
            task.cleanSprintStatus();
            return;
        }
        task.updateAssignedUser(emailOfAssignedUser);
    }

    public void updateTaskEstimatedDuration(UUID taskId, int estimatedDuration) {
        Task task = backlog.getTasks().stream().filter(t -> t.getId().equals(taskId)).findFirst().orElseThrow(() -> new RuntimeException("Task could not found in backlog"));
        task.updateEstimatedDuration(estimatedDuration);
    }

    public void updateTaskStatus(UUID taskId, TaskStatus status) {
        Task task = backlog.getTasks().stream().filter(t -> t.getId().equals(taskId)).findFirst().orElseThrow(() -> new RuntimeException("Task could not found in backlog"));
        if (task.getEmailOfAssignedUser() == null) {
            task.cleanSprintStatus();
            return;
        }
        if (status == TaskStatus.UNASSIGNED)
            return;
        task.updateStatus(status);
    }

    public Task getTaskById(UUID taskId) {
        return backlog.getTasks().stream().filter(t -> t.getId().equals(taskId)).findFirst().orElseThrow(() -> new RuntimeException("Task could not found in backlog"));
    }

    public List<Task> getTasksOfUser(String email) {
        return backlog.getTasks().stream().filter(t -> Objects.equals(t.getEmailOfAssignedUser(), email)).toList();
    }

    public void updateName(String newName) {
        if (newName.isEmpty()) {
            throw new IllegalArgumentException("The specified name cannot be empty.");
        }
        this.name = newName;
    }

    public void updateDescription(String description) {
        if (description.isEmpty()) {
            throw new IllegalArgumentException("The specified description cannot be empty.");
        }
        this.description = description;
    }

    public void updateManager(String emailOfManager) {
        if (emailOfManager == null || emailOfManager.isEmpty()) {
            throw new IllegalArgumentException("The specified manager cannot be null or empty.");
        }
        if (emailOfTeamLead != null && emailOfTeamLead.equals(emailOfManager)) {
            throw new IllegalArgumentException("Team lead cannot be the same as the manager.");
        }
        if (emailsOfMembers.contains(emailOfManager)) {
            throw new IllegalArgumentException("The specified manager is already a member of this project.");
        }
        this.emailOfManager = emailOfManager;
    }

    public void updateTeamLead(String emailOfTeamLead) {
        if (emailOfManager != null && emailOfManager.equals(emailOfTeamLead)) {
            throw new IllegalArgumentException("Manager cannot be the same as the team lead.");
        }
        if (emailsOfMembers.contains(emailOfTeamLead)) {
            throw new IllegalArgumentException("The specified team lead is already a member of this project.");
        }
        moveTasksOnTeamLeadChange(emailOfTeamLead);
        this.emailOfTeamLead = emailOfTeamLead;
    }

    private void moveTasksOnTeamLeadChange(String emailOfTeamLead) {
        var tasksOnTeamLead = backlog.getTasks().stream().filter(task -> Objects.equals(task.getEmailOfAssignedUser(), emailOfTeamLead)).toList();
        tasksOnTeamLead.forEach(task -> task.updateAssignedUser(emailOfManager));
    }

    public void updateSprintDuration(int sprintDurationInWeeks) {
        if (sprintDurationInWeeks < 1) {
            throw new IllegalArgumentException("The specified sprint duration cannot be less than 1.");
        }
        if (this.sprintDurationInWeeks == sprintDurationInWeeks)
            return;
        this.sprintDurationInWeeks = sprintDurationInWeeks;
    }

    public void updateStartDate(LocalDate startDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("The specified start date cannot be null.");
        }
        if (startDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("The specified start date cannot be before today.");
        }
        if (this.startDate == null) {
            this.startDate = startDate;
            createSprint();
            return;
        }
        if (this.startDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Cannot change the start date of started project.");
        }
        if (startDate.isAfter(this.startDate)) {
            if (this.endDate == null) {
                this.startDate = startDate;
                updateSprintsDates(startDate);
                return;
            }
            if (startDate.isAfter(this.endDate)) {
                throw new IllegalArgumentException("Start date cannot be after the end date.");
            }
            this.startDate = startDate;
            updateSprintsDates(startDate);
            return;
        }
        if (startDate.isBefore(this.startDate)) {
            this.startDate = startDate;
            updateSprintsDates(startDate);
        }
    }

    private void updateSprintsDates(LocalDate startDate) {
        this.sprints.forEach(sprint -> {
            var calculatedStartDate = startDate.plusWeeks((long) (sprint.getSprintNumber() - 1) * sprintDurationInWeeks);
            sprint.updateDates(calculatedStartDate, calculatedStartDate.plusWeeks(sprintDurationInWeeks).minusDays(1));
        });
    }

    public void updateEndDate(LocalDate endDate) {
        if (endDate == null) {
            throw new IllegalArgumentException("The specified end date cannot be null.");
        }
        if (endDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("The specified end date cannot be before today.");
        }
        if (this.startDate != null && endDate.isBefore(this.startDate)) {
            throw new IllegalArgumentException("End date cannot be before the start date.");
        }
        this.endDate = endDate;
    }

    public void startNextSprint() {
        var notCompletedSprints = sprints.stream().filter(sprint -> !sprint.isCompleted()).toList();
        if (notCompletedSprints.isEmpty()) {
            throw new SprintNotFoundException("There is no sprint to start.");
        }
        var nextSprint = notCompletedSprints.getFirst();
        nextSprint.start();
    }

    public void endCurrentSprint() {
        var currentSprint = getCurrentSprint();
        if (!Objects.equals(currentSprint.getEndDate(), LocalDate.now())) {
            var sprintsToUpdate = sprints.stream().filter(sprint -> sprint.getSprintNumber() > currentSprint.getSprintNumber()).toList();
            currentSprint.updateDates(currentSprint.getStartDate(), LocalDate.now());
            var calculatedStartDate = currentSprint.getEndDate().plusDays(1);
            var calculatedEndDate = calculatedStartDate.plusWeeks(sprintDurationInWeeks).minusDays(1);
            for (var sprint : sprintsToUpdate) {
                sprint.updateDates(calculatedStartDate, calculatedEndDate);
                calculatedStartDate = calculatedStartDate.plusWeeks(sprintDurationInWeeks);
                calculatedEndDate = calculatedStartDate.plusWeeks(sprintDurationInWeeks);
            }
        }
        currentSprint.end();
    }

    private Sprint getCurrentSprint() {
        return sprints.stream().filter(Sprint::isActive).findFirst().orElseThrow(() -> new RuntimeException("There is no active sprint in this project."));
    }

    public void deleteSprint(int sprintNumber) {
        if (sprints.isEmpty()) {
            throw new SprintNotFoundException("There is no sprint in this project.", this.id, sprintNumber);
        }
        if (sprintNumber < 1) {
            throw new SprintNotFoundException("Sprint could not found in this project.", this.id, sprintNumber);
        }
        var sprintToDelete = sprints.stream().filter(sprint -> sprint.getSprintNumber() == sprintNumber).findFirst().orElseThrow(() -> new SprintNotFoundException("The specified sprint could not found in this project.", this.id, sprintNumber));
        sprintToDelete.removeAllTasks();
        sprints.remove(sprintToDelete);
    }

    public static Project createProject(String name, String description, String emailOfManager, String emailOfTeamLead, List<String> emailsOfMembers, LocalDate startDate, LocalDate endDate, int sprintDurationInWeeks) {
        var project = new Project();
        project.setName(name);
        project.description = description;
        project.setEmailOfManager(emailOfManager);
        project.emailOfTeamLead = emailOfTeamLead;
        project.emailsOfMembers = emailsOfMembers;
        project.startDate = startDate;
        project.endDate = endDate;
        project.setSprintDurationInWeeks(sprintDurationInWeeks);
        project.createBacklog();
        if (project.startDate != null) {
            project.createSprint();
        }
        return project;
    }

    public static class Builder {
        private final String name;
        private final String emailOfManager;
        private final int sprintDurationInWeeks;

        private String description;
        private String emailOfTeamLead;
        private List<String> emailsOfMembers = new ArrayList<>();
        private LocalDate startDate;
        private LocalDate endDate;

        public Builder(String name, String emailOfManager, int sprintDurationInWeeks) {
            this.name = name;
            this.emailOfManager = emailOfManager;
            this.sprintDurationInWeeks = sprintDurationInWeeks;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder emailOfTeamLead(String emailOfTeamLead) {
            this.emailOfTeamLead = emailOfTeamLead;
            return this;
        }

        public Builder emailsOfMembers(List<String> emailsOfMembers) {
            if (emailsOfMembers == null) {
                this.emailsOfMembers = new ArrayList<>();
            } else {
                this.emailsOfMembers = new ArrayList<>(emailsOfMembers);
            }
            return this;
        }

        public Builder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public Project build() {
            return Project.createProject(name, description, emailOfManager, emailOfTeamLead, emailsOfMembers, startDate, endDate, sprintDurationInWeeks);
        }
    }

    private void setName(String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("The specified name cannot be empty.");
        }
        this.name = name;
    }

    private void setEmailOfManager(String emailOfManager) {
        if (emailOfManager == null || emailOfManager.isEmpty()) {
            throw new IllegalArgumentException("The specified manager cannot be null or empty.");
        }
        this.emailOfManager = emailOfManager;
    }

    private void setSprintDurationInWeeks(int sprintDurationInWeeks) {
        if (sprintDurationInWeeks < 1) {
            throw new IllegalArgumentException("The specified sprint duration cannot be less than 1.");
        }
        this.sprintDurationInWeeks = sprintDurationInWeeks;
    }
}
