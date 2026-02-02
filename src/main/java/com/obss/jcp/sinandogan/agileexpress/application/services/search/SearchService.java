package com.obss.jcp.sinandogan.agileexpress.application.services.search;

import com.obss.jcp.sinandogan.agileexpress.application.services.search.models.ProjectSearch;
import com.obss.jcp.sinandogan.agileexpress.application.services.search.models.TaskSearch;
import com.obss.jcp.sinandogan.agileexpress.application.services.search.models.requestresponse.SearchResponseModel;

import java.util.List;
import java.util.UUID;

public interface SearchService {

    List<ProjectSearch> searchProjects(String query, String email);

    List<TaskSearch> searchTasks(String query, String email);

    SearchResponseModel search(String query, String email);

    void updateProjectSearch(UUID projectId);

    void deleteProjectSearch(UUID projectId);

    void updateTaskSearch(UUID taskId);

    void deleteTaskSearch(UUID taskId);
}
