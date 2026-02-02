package com.obss.jcp.sinandogan.agileexpress.infrastructure.search.mappers;

import com.obss.jcp.sinandogan.agileexpress.application.services.search.models.ProjectSearch;
import com.obss.jcp.sinandogan.agileexpress.application.services.search.models.TaskSearch;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.search.entity.models.ProjectSearchDocument;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.search.entity.models.TaskSearchDocument;

public class SearchMapper {
    public static ProjectSearch toDomain(ProjectSearchDocument document) {
        if (document == null) return null;
        return new ProjectSearch(document.getId(), document.getProjectId(), document.getName(), document.getDescription(), document.getCreatedAt(), document.getUpdatedAt());

    }

    public static ProjectSearchDocument toDocument(ProjectSearch projectSearch) {
        if (projectSearch == null) return null;
        return new ProjectSearchDocument(projectSearch.getId(), projectSearch.getProjectId(), projectSearch.getName(), projectSearch.getDescription(), projectSearch.getCreatedAt(), projectSearch.getUpdatedAt());
    }

    public static TaskSearch toDomain(TaskSearchDocument document) {
        if (document == null) return null;
        return new TaskSearch(document.getId(), document.getTaskId(), document.getProjectId(), document.getName(), document.getDescription(), document.getAssignedUserEmail(), document.getAssignedUserName(), document.getCreatedAt(), document.getUpdatedAt());

    }

    public static TaskSearchDocument toDocument(TaskSearch taskSearch) {
        if (taskSearch == null) return null;
        return new TaskSearchDocument(taskSearch.getId(), taskSearch.getTaskId(), taskSearch.getProjectId(), taskSearch.getName(), taskSearch.getDescription(), taskSearch.getAssignedUserEmail(), taskSearch.getAssignedUserName(), taskSearch.getCreatedAt(), taskSearch.getUpdatedAt());
    }
}
