package com.obss.jcp.sinandogan.agileexpress.application.services.project.models.shared;

import com.obss.jcp.sinandogan.agileexpress.domain.enums.StoryPoint;
import com.obss.jcp.sinandogan.agileexpress.domain.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskResponseModel {
    UUID id;
    String name;
    String description;
    String emailOfAssignedUser;
    String emailOfCreatedUser;
    StoryPoint storyPoint;
    TaskStatus status;
    int estimatedDuration;
    List<WorkEntryResponseModel> workEntries;
    UUID sprintId;
    UUID backlogId;
}
