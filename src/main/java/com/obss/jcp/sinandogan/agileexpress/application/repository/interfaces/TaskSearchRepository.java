package com.obss.jcp.sinandogan.agileexpress.application.repository.interfaces;

import com.obss.jcp.sinandogan.agileexpress.application.services.search.models.TaskSearch;

import java.util.List;

public interface TaskSearchRepository {
    List<TaskSearch> searchByQueryAndProjectIds(String query, List<String> authorizedProjectIds);

    void save(TaskSearch taskSearch);

    void deleteById(String string);
}
