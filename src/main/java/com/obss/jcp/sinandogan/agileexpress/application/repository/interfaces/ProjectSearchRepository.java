package com.obss.jcp.sinandogan.agileexpress.application.repository.interfaces;

import com.obss.jcp.sinandogan.agileexpress.application.services.search.models.ProjectSearch;

import java.util.List;

public interface ProjectSearchRepository {
    List<ProjectSearch> searchByQueryAndProjectIds(String query, List<String> authorizedProjectIds);

    void save(ProjectSearch projectSearch);

    void deleteById(String string);
}
