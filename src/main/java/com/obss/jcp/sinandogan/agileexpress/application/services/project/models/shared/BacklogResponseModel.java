package com.obss.jcp.sinandogan.agileexpress.application.services.project.models.shared;

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
public class BacklogResponseModel {
    UUID id;
    UUID projectId;
    List<TaskResponseModel> tasks;
}
