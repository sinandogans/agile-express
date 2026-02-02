package com.obss.jcp.sinandogan.agileexpress.infrastructure.search.repositories;

import com.obss.jcp.sinandogan.agileexpress.application.repository.interfaces.ProjectSearchRepository;
import com.obss.jcp.sinandogan.agileexpress.application.services.search.models.ProjectSearch;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.search.mappers.SearchMapper;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.search.repositories.mongo.MongoProjectSearchRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProjectSearchRepositoryImpl implements ProjectSearchRepository {
    private final MongoProjectSearchRepository mongoProjectSearchRepository;

    public ProjectSearchRepositoryImpl(MongoProjectSearchRepository mongoProjectSearchRepository) {
        this.mongoProjectSearchRepository = mongoProjectSearchRepository;
    }

    @Override
    public List<ProjectSearch> searchByQueryAndProjectIds(String query, List<String> authorizedProjectIds) {
        var documents = mongoProjectSearchRepository.searchByQueryAndProjectIds(query, authorizedProjectIds);
        var searches = new ArrayList<ProjectSearch>();
        documents.forEach(document -> searches.add(SearchMapper.toDomain(document)));
        return searches;
    }

    @Override
    public void save(ProjectSearch projectSearch) {
        mongoProjectSearchRepository.save(SearchMapper.toDocument(projectSearch));
    }

    @Override
    public void deleteById(String string) {
        mongoProjectSearchRepository.deleteById(string);
    }
}
