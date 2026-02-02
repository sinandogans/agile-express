package com.obss.jcp.sinandogan.agileexpress.application.services.search.models.requestresponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskSearchModel {
    private String name;
    private String taskId;
    private String projectId;
}
