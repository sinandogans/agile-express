package com.obss.jcp.sinandogan.agileexpress.application.services.project.dtos.shared;

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
public class TaskResponseDto {
    UUID id;
    String name;
    String description;
    String emailOfAssignedUser;
    String emailOfCreatedUser;
    StoryPoint storyPoint;
    TaskStatus status;
    List<WorkEntryResponseDto> workEntries;
    int estimatedDuration;
    UUID sprintId;
    UUID backlogId;
}
