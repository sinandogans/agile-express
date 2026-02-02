package com.obss.jcp.sinandogan.agileexpress.infrastructure.search.repositories;

import com.obss.jcp.sinandogan.agileexpress.application.repository.interfaces.TaskSearchRepository;
import com.obss.jcp.sinandogan.agileexpress.application.services.search.models.TaskSearch;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.search.mappers.SearchMapper;
import com.obss.jcp.sinandogan.agileexpress.infrastructure.search.repositories.mongo.MongoTaskSearchRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TaskSearchRepositoryImpl implements TaskSearchRepository {
    private final MongoTaskSearchRepository mongoTaskSearchRepository;

    public TaskSearchRepositoryImpl(MongoTaskSearchRepository mongoTaskSearchRepository) {
        this.mongoTaskSearchRepository = mongoTaskSearchRepository;
    }

    @Override
    public List<TaskSearch> searchByQueryAndProjectIds(String query, List<String> authorizedProjectIds) {
        var documents = mongoTaskSearchRepository.searchByQueryAndProjectIds(query, authorizedProjectIds);
        var searches = new ArrayList<TaskSearch>();
        documents.forEach(document -> searches.add(SearchMapper.toDomain(document)));
        return searches;
    }

    @Override
    public void save(TaskSearch taskSearch) {
        mongoTaskSearchRepository.save(SearchMapper.toDocument(taskSearch));
    }

    @Override
    public void deleteById(String string) {
        mongoTaskSearchRepository.deleteById(string);
    }
}
