package com.obss.jcp.sinandogan.agileexpress.application.services.project.tasks.models.update;

import com.obss.jcp.sinandogan.agileexpress.domain.enums.StoryPoint;
import com.obss.jcp.sinandogan.agileexpress.domain.enums.TaskStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateTaskModel {
    private UUID taskId;
    private String name;
    private String description;
    private StoryPoint storyPoint;
    private String emailOfAssignedUser;
    private int estimatedDuration;
    private int actualDuration;
    private TaskStatus status;
    private String email;
}
