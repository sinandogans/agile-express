package com.obss.jcp.sinandogan.agileexpress.application.services.project.tasks.models.delete;

import com.obss.jcp.sinandogan.agileexpress.domain.enums.StoryPoint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DeleteTaskResponseModel {
    UUID taskId;
}
