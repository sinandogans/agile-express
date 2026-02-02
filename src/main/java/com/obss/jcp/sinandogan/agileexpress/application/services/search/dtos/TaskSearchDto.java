package com.obss.jcp.sinandogan.agileexpress.application.services.search.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskSearchDto {
    private String name;
    private String taskId;
    private String projectId;
}
